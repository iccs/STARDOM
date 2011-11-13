<!-- freemarker macros have to be imported into a namespace.  We strongly
recommend sticking to 'spring' -->
<#import "spring.ftl" as spring/>
<#import "macro/iccs.ftl" as iccs/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"

<head>
    <title>Identity Representation</title>
    <link rel="stylesheet/less" type="text/css" href="<@spring.url "/static/css/main.less"/>">
    <script src="<@spring.url "/static/js/less-1.1.3.min.js" />" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/stardom.js" />" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/jquery-1.6.1-min.js"/>" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/jquery.timers-1.2.js"/>" type="text/javascript"></script>
</head>
<body>
<script type="text/javascript">
    $(document).ready(function(){

        var timer = 5000;
        var page = 0;


        $('.page-link').click(function(eventObject){

            if(ICCS.isLocked(this)){
                console.log('Disabled or is selected');
                return;
            }

            $('#view').stopTime('view');

            ICCS.lockLink(this);

            $(this).attr('enable','false');
            var url = $(this).attr("title");
            console.log('click '+url);

            $(".selected").removeClass('selected');
            $(this).parent().toggleClass('selected');
            var link = $(this);
            ICCS.setView(link,url,"#view","#pages","#list-loader");

            $('#view').everyTime(timer,'view',function(){
                ICCS.setView(null,"/ui/ui/list/current","#view","#pages","#list-loader");
            });

            return false;

        });

        $('.page-link.previous').trigger('click');


        var url = "/ws/constructor/events/count";
        $('#events-processed').everyTime(timer,'events',function(){

            $('#events-loader').fadeIn();
            $.getJSON(url,function(data){

                $("#scm-events").html(data.scm);
                $("#its-events").html(data.its);
                $("#ml-events").html(data.ml);

                $('#events-loader').fadeOut();

            });

        });

        $('#log').everyTime(timer,'logs',function(){

            $('#logs-loader').fadeIn();
            console.log('timer-log');
            var url = '<@spring.url "/log/dump"/>';
            $.get(url,function(data){
                $("#log").html(data);
                $('#logs-loader').fadeOut();
            });
        });
    });

</script>
    <div class="container">

        <div id="events-processed" >
            <h2>Events Processed <@iccs.loader id="events"/></h2>
            <div style="clear:both"><label>SCM: </label><span  id="scm-events">0</span></div>
            <div style="clear:both"><label>ITS: </label><span id="its-events">0</span></div>
            <div style="clear:both"><label>ML : </label><span id="ml-events">0</span></div>
        </div>
        <div id="log-container">
            <h2>Process Output <@iccs.loader id="logs"/></h2>
            <div id="log" style="">
            </div>
        </div>
        <div style="clear:both"/>
        <#include "ui/prevnext.ftl"/>
        <div id="view"></div>

</body>
</html>