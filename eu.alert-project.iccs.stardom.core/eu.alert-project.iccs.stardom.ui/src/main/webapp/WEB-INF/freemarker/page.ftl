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

        console.log("Ready")

        $('.page-link').click(function(eventObject){

            if(ICCS.isLocked(this)){
                console.log('Disabled or is selected');
                return;
            }

            ICCS.lockLink(this);

            $(this).attr('enable','false');
            $('#list-loader').fadeIn();

            var url = $(this).attr("title");
            console.log('click '+url);

            $(".selected").removeClass('selected');
            $(this).parent().toggleClass('selected');
            var link = $(this);

            $.get(url,function(data){

                ICCS.unlockLink(link);
                $('#list-loader').fadeOut();
                $('#view').html(data);

                $.getJSON("/ui/pagination.json",function(data){

                    var selectedPage = data.selected;
                    var pages= data.pages;

                    var result = "<ul class=\"pages\">";

                    for(var i=0; i< pages.length; i++){

                        var p =pages[i];
                        result+="<li class=\"page "+(selectedPage==p ? 'selected':'')+"\">"+p+"</li>";
                    }
                    result +="</ul>";

                    $('#pages').html(result);
                });

            });
            return false;

        });

        $('.page-link.previous').trigger('click');

        $('#events-processed').everyTime(10000,'events',function(){
            $('#events-loader').fadeIn();

            $('#events-processed').oneTime(5000,function(){
                $('#events-loader').fadeOut();
            })
        });

    });

</script>
    <div class="container">

        <div id="events-processed" >
            <h2>Events Processed <@iccs.loader id="events"/></h2>
            <label>SCM: </label><span  id="scm-events">0</span><br/>
            <label>ITS: </label><span id="its-events">0</span><br/>
            <label>ML : </label><span id="ml-events">0</span><br/>
        </div>
        <div style="clear:both"/>
        <#include "ui/prevnext.ftl"/>
        <div id="view"></div>
</body>
</html>