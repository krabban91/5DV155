function setupGAM(){
    $('<link rel="stylesheet" type="text/css" href="/globalalerts/css/gam.css" >').appendTo("head");
    //production defaults
    var confIntervDef = 900000;
    var messageIntervDef = 90000;
    // development defaults
    //var confIntervDef = 30000;
    //var messageIntervDef = 10000;
    var LowBoundMess = 10000; 
    var LowBoundConf = 30000;
    var conf_results ="";
    var messageArray = "";
    var lastGet="initial";

    // handler for dismissing level 2 and 3 messages
    $(document).on("click", ".dismissLink a", function(e) {
        var target = $(this).attr('id');
        var timestamp = $(this).attr('class');
        e.preventDefault();
        writeDOMVal(target, timestamp);
        $(this).closest('.portalMessage').fadeOut('slow');
        $(this).closest('.portalMessage').remove();
    });
    // handler for minimizing level 1 messages
    $(document).on("click", ".minimLink a", function(e) {
        var target = $(this).attr('id');
        var timestamp = $(this).attr('class');
        e.preventDefault();
         if ($(this).closest('.portalMessage').parent('#portalMessageMinimContainer').length === 0) {
            $(this).closest('.portalMessage').appendTo('#portalMessageMinimContainer');
            $(this).text('^');
        }
        else {
            $(this).closest('.portalMessage').appendTo('#portalMessageContainer1_2');
            $(this).text('v');
        }
    });

	//UmU-hack, do not get the config
	conf_results={};
    // used by the initial configuration settings request
/*    $.ajax({    
        url: '/globalalerts/config?lastGET=' + lastGet ,
        type: 'get',
        dataType: 'json',
        async: false,
        success: function(data){
            conf_results = data;
        },
        error: function(){
            //log('error on INITIAL config request - bad config data, return an empty object');
            conf_results = {};
            lastGet="error-on-request";
        }
    });
  */  

    //if there is no conf file, use defaults
    if ($.isEmptyObject(conf_results)) {
        //log('blank conf file (empty object returned) on INITIAL conf request, set to defaults which are conf=' + confIntervDef + ', messageIntervDef=' + messageIntervDef);
        messageInterv = messageIntervDef;
        confInterv = confIntervDef;
        lastGet="empty";
    }
    else {
        if (conf_results.message_wait < LowBoundMess) {
            messageInterv = messageIntervDef;
            lastGet="out-of-range";
            //log('message polling interval less than ' + LowBoundMess  + ' - so setting to defaults');
        }
        else {
            messageInterv = conf_results.message_wait;
        }
        if (conf_results.config_wait < LowBoundConf) {
            //log('config polling interval less than ' + LowBoundConf + ' - so setting to defaults');
            lastGet="out-of-range";
            confInterv = confIntervDef;
        }
        else {
            confInterv = conf_results.config_wait;
            lastGet=confInterv;
        }
    }

    //setting a timer for messages
    var msgIntervId = setInterval(function(){
        msgTimer();
    }, LowBoundMess);
    
    //setting a timer for config details
    var confIntervId = setInterval(function(){
         confTimer();
    }, confInterv);
    
    /* retrieve messages, reseting message timer
     * in case this has changed
     */
    function msgTimer(){
        var resultsMsg = $.ajax({
            url: '/globalalerts/messages',
            type: 'get',
            dataType: 'json'
            //async: false,
        }).done(function(data){
            if (data) {
                // will need to check for {} {wspace} {malformed}
                processMessages(data);
            }
            clearInterval(msgIntervId);
            msgIntervId = setInterval(function(){
                msgTimer();
            }, messageInterv);
        });
        ;
    }
    
    
    /* poll configuration data, 
     * reset values if the Configuration interval has changed
     */
    function confTimer(){
        var resultsConf = $.ajax({
            url: '/globalalerts/config?lastGET=' + lastGet,
            type: 'get',
            dataType: 'json'
            //async: false,
        }).done(function(data){
        
            // there is no configuration file?
            if ($.isEmptyObject(data)) {
                //log('blank conf file on SUBSEQUENT conf request, set to defaults which are conf=' + confIntervDef + ', messageIntervDef=' + messageIntervDef);
                lastGet = "empty";
                messageInterv = messageIntervDef;
                confInterv = confIntervDef;
            }
            else {
                if (data.config_wait !== confInterv) {
                    // check to see if data.config_wait < LowBoundConf and if so set to the defaults 
                    if (data.config_wait < LowBoundConf) {
                        //log("config interv changed to < " + LowBoundConf + " so setting to default");
                        lastGet = "out-of-range";
                        confInterv = confIntervDef;
                    }
                    else {
                        confInterv = data.config_wait;
                        lastGet = confInterv;
                    }
                    //log('----- conf polling interval has CHANGED - new value: ' + confInterv);
                    clearInterval(confIntervId);
                    confIntervId = setInterval(function(){
                        confTimer();
                    }, confInterv);
                }
                else {
                    lastGet = confInterv;
                    //log('----- conf polling interval - UNCHANGED, still at ' + confInterv);
                }
                if (messageInterv !== data.message_wait) {
                    //log('----- messageInterv  - CHANGED - ' + messageInterv + '(OLD) ' + data.message_wait + '(NEW)');
                }
                else {
                    //log('----- messageInterv - UNCHANGED - still at ' + messageInterv);
                }
                // check to see if data.message_wait < 60000 and if so set to the defaults AAA
                if (data.message_wait < LowBoundMess) {
                    //log("message interv changed to < " + LowBoundMess + " so setting to default");
                    lastGet = "out-of-range";
                    messageInterv = messageIntervDef;
                }
                else {
                    messageInterv = data.message_wait;
                }
                
                
            }
        }).fail(function(data){
            //log('error on SUBSEQUENT config request - bad config data, change nothing');
            lastGet = "error";
        });
        ;
    }
    var processMessages = function(data){
        messageArray ='';

        if (data.messages.length > 0) {
            if ($("#portalMessageContainer1_2").length === 0) {
                $("#portalOuterContainer").append('<div id=\"portalMessageContainer1_2\" role=\"application\" aria-live\"assertive\" aria-relevant=\"additions\"></div>');
                $("#portalOuterContainer").append('<div id=\"portalMessageMinimContainer\" role=\"application\" aria-live\"assertive\" aria-relevant=\"additions\"></div>');
            }
            if ($("#portalMessageContainer3").length === 0) {
                $("#portalOuterContainer").append('<div id=\"portalMessageContainer3\" role=\"application\" aria-live\"assertive\" aria-relevant=\"additions\"></div>');
            }
            $('#portalMessageMinimContainer .portalMessage .minimLink a').text('v');
            $('#portalMessageMinimContainer .portalMessage').appendTo('#portalMessageContainer1_2');
            $.each(data.messages, function(i, item){
                var messageLink = '';
                
                // create a safe ID
                var itemId = encodeURIComponent(item.id).replace(/[^a-zA-Z 0-9]+/g, '-');
                                    messageArray = messageArray + '_' + itemId;
                // need to check if displayed already - is it in the DOM?
                // TODO: need to check if "was" in the DOM and has been dismissed
                if ($('#' + itemId).length > 0) {
                    //elem there already, compare timestamps
                    if ($('#' + itemId).attr('ts') !== item.timestamp) {
                        // message has changed, update it, animate it (and update the ts attribute)
                        flashMessage($('#' + itemId), item.message);
                        $('#' + itemId).attr('ts', item.timestamp);
                    }
                }
                else {
                 // add meessage ID to a string, this should be an actual array instead of a string
                // should also be added only if it is not there (not needed as start from scratch each parse?)

                    if (item.priority > 1) {
                        messageLink = '<div class=\"dismissLink\"><a href=\"#\" id=\"' + itemId + '\"class=\"' + item.timestamp + '\">x</a></div>';
                    }
                    else {
                        messageLink = '<div class=\"minimLink\"><a href=\"#\" id=\"' + itemId + '\"class=\"' + item.timestamp + '\">v</a></div>';
                    }

                    // test for cookie with this safe id as value
                    if (!readDOMVal(itemId)) {
                        //no cookie - so new message, add to DOM
                        if (item.priority === 3) {
                            $('#portalMessageContainer3').append('<div role=\"alert\" aria-live\"assertive\" aria-relevant=\"text\" id=\"' + itemId + '\" ts=\"' + item.timestamp + '\" class=\"portalMessage portalMessageShadow portalMessagePriority' + item.priority + '\"><div class=\"messageHolder\">' + item.message + '</div>' + messageLink + '</div>');
                        }
                        else {
                            $('#portalMessageContainer1_2').append('<div role=\"alert\" aria-live\"assertive\" aria-relevant=\"text\" id=\"' + itemId + '\" ts=\"' + item.timestamp + '\" class=\"portalMessage portalMessageShadow portalMessagePriority' + item.priority + '\"><div class=\"messageHolder\">' + item.message + '</div>' + messageLink + '</div>');
                        }
                    }
                    else {
                        //message has been dismissed, but has been updated - so re-add to DOM
                        
                        if (readDOMVal(itemId) !== item.timestamp) {
                            if (item.priority === 3) {
                                $('#portalMessageContainer3').append('<div role=\"alert\" aria-live\"assertive\" aria-relevant=\"text\" id=\"' + itemId + '\" ts=\"' + item.timestamp + '\" class=\"portalMessage portalMessageShadow portalMessagePriority' + item.priority + '\"><div class=\"messageHolder\">' + item.message + '</div>' + messageLink + '</div>');
                            }
                            else {
                                $('#portalMessageContainer1_2').append('<div role=\"alert\" aria-live\"assertive\" aria-relevant=\"text\" id=\"' + itemId + '\" ts=\"' + item.timestamp + '\" class=\"portalMessage portalMessageShadow portalMessagePriority' + item.priority + '\"><div class=\"messageHolder\">' + item.message + '</div>' + messageLink + '</div>');
                            }
                        }
                        else {
                            if (item.priority === 1) {
                                $('#portalMessageContainer1_2').append('<div role=\"alert\" aria-live\"assertive\" aria-relevant=\"text\" id=\"' + itemId + '\" ts=\"' + item.timestamp + '\" class=\"portalMessage portalMessageShadow portalMessagePriority' + item.priority + '\"><div class=\"messageHolder\">' + item.message + '</div>' + messageLink + '</div>');
                            }
                        }
                    }
                }

            });
        
        }
        else {
           $("#portalMessageContainer1_2").remove();
           $("#portalMessageContainer3").remove();
        }

        if ($(".portalMessage").length) {
            $(".portalMessage").each(function(i){
                //if the id of this elem is not in the message bundle just processed, remove it from the DOM
                if (messageArray.indexOf($(this).attr('id')) === -1) {
                    var reg = new RegExp($(this).attr('id'), "g");
                    messageArray = messageArray.replace(reg, "");
                    $(this).fadeOut(2000, function(){
                        $(this).remove();
                    });
                }
            });
           
        }
        
        function flashMessage(elem, message){
            if ($(elem).parent('#portalMessageMinimContainer').length === 1) {
                $(elem).appendTo('#portalMessageContainer1_2');
                $(elem).find('.minimLink').find('span').text('v');
            }
            $(elem).removeClass('portalMessageShadow');
            $(elem).fadeOut(2000, function(){
                $(this).children('.messageHolder').empty();
                $(this).children('.messageHolder').append('<div>' + message + '</div>');
                $(this).fadeIn(2000, function(){
                    $(this).addClass('portalMessageShadow');
                });
            });
        }
};

/* utils for getting/setting DOM storage
 *  - not sure what will use for other than a replacement 
 *  for cookies
 */
    var readDOMVal = function(name){
        if (window.localStorage) {
            return sessionStorage.getItem([name]);
        }
    };
    var writeDOMVal = function(name, val){
        if (window.localStorage) {
            sessionStorage.setItem([name], val);
        }
    };
    
}
