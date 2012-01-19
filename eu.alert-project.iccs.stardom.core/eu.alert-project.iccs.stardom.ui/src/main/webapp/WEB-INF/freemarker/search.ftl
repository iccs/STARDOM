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
<body class="search">
<script type="text/javascript">

    $(document).ready(function(){
        $('#search-text').keyup('keypress', function(e) {
            if(e.keyCode==13){
                find();
            }else if(e.keyCode==27){
                $(this).val("");
                $("#search-results").html("<h3>Type a search term and press ENTER</h3>");
            }
        });

        $('#search-text').focus();
    });

    function remove(a){
        $(a).parent().remove();
    }


    function find(){

        if(ICCS.isLocked("#search-button")){
            return;
        }
        $("#search-loader").fadeIn();

        var text = $("#search-text").val();

        $.getJSON(
                "<@spring.url "/search"/>/"+text,
                function(data){

                    if(data.results == undefined){
                        ICCS.unlockLink("#search-button","Search");
                        return;
                    }

                    var ul = "";
                    var counter =0;
                    $.each(data.results,function(key,val){


                        ul+='<div class="result ">' +
                                "<label style='float: left'>"+key+"</label> " +
                                "<a class='delete-link' style='float: right;clear:right;' href='#' onclick='remove(this);return false;'>Delete</a>" +
                                '<div class="profiles">';
                                
                        ul+="<table class='profiles'>";

                        $.each(val.profiles,function(pkey,pval){
                            ul+="<tr> " +
                                    "<td>"+pval.id+"</td>" +
                                    "<td>"+pval.name+"</td>" +
                                    "<td>"+pval.lastname+"</td>" +
                                    "<td>"+pval.username+"</td>" +
                                    "<td>"+pval.email+"</td>" +
                                    "<td>"+pval.source+"</td>" +
                                "</tr>";
                            
                        });
                        ul+="</table>";

                        ul+="<table class='metrics'>";

                        $.each(val.metrics,function(pkey,pval){
                            ul+="<tr> " +
                                    "<td>"+pval.name+"</td>" +
                                    "<td>"+pval.value+"</td>" +
                                "</tr>";

                        });

                        ul+="</table>";

                        ul+=    "</div>" +
                            "</div>"


                        counter++;
                        if(counter % 2 == 0){
//                            ul +='<div style="clear:both"></div>'
                        }

                    });
                    
                    $("#search-results").html(ul);



                    $("#search-loader").fadeOut(function(){
                        ICCS.unlockLink("#search-button","Search");
                    });
                }
        );



        ICCS.lockLink("#search-button","");


    }

</script>
    <div class="container">

        <h1> Type your search query </h1>
        <div id="search-control">
            <input type="text" id="search-text">
            <a id="search-button" href="#" onclick="find();return false;">Search</a>
            <@iccs.loader id="search"/>
        </div>
        <div id="search-results">
            <h3>Type a search term and press ENTER</h3>
        </div>
    </div>
</body>
</html>