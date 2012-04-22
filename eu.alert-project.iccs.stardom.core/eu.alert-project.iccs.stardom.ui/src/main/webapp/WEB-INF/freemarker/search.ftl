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

    function searchQuantity(){
        doSearch("#search-quantity-button","<@spring.url "/search/quantitative"/>/"+getSearchString());
    }

    function searchName(){
        doSearch("#search-name-button","<@spring.url "/search"/>/"+getSearchString());
    }


    function toggleSelected(id){

        if($("#merge-ids").has("#ck-"+id).size() > 0){
            console.log("It has the id!!!");
            $("#ck-"+id).remove();
        }else{

            $('<input>').attr({
                type: 'hidden',
                id: 'ck-'+id,
                name: "ids",
                value: id
            }).appendTo('#merge-ids');
        }

        var visible = $("#merge-ids").has("#ck-"+id).size() > 0;

        $("#merge-button").toggle(visible);

    }


    function doSearch(link,url){

        if(ICCS.isLocked(link)){
            return;
        }

        $("#merge-ids").html("");


        lockLinks();

        $.getJSON(
            url,
            function(data){

                if(data.results == undefined){
                    unlockLinks();
                    return;
                }

                var ul = "";
                var counter =0;

                var empty = $.isEmptyObject(data.results);
                console.log("Empty "+empty)
                if(!empty){
                    
                    $.each(data.results,function(key,val){
    
                        ul+='<div class="result ">' +
                                "<label style='float: left'>"+key+" ("+val.identity+")</label> " +
                                "<a class='delete-link' style='float: right;clear:right;' href='#' onclick='remove(this);return false;'>Delete</a>" +
                                "<input type='checkbox' onclick=\"toggleSelected('"+val.identity+"')\" />"+
                                '<div class="profiles">';
    
                        ul+="<table class='profiles'>";
                        ul+="<thead><tr> " +
                                "<th>Id</td>" +
                                "<th>Name</td>" +
                                "<th>Lastname</td>" +
                                "<th>Username</td>" +
                                "<th>Email</td>" +
                                "<th>Source</td>" +
                            "</tr></thead><tbody>";
    
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
                        ul+="</tbody></table>";
    
                        ul+="<table class='metrics'>";
    
                        $.each(val.metrics,function(pkey,pval){
                            ul+="<tr> " +
                                    "<td>"+pval.name+"</td>" +
                                    "<td>"+pval.value+"</td>" +
                                "</tr>";
    
                        });
    
                        ul+="</table>" +
                                "</div>" +
                            "</div>";
    
    
                        counter++;
    
                    });
    
                    $("#search-results").html(ul);
                }else{

                    $("#search-results").html("<a href='<@spring.url "/login"/>' >Create a profile!</a>");
                        
                }

                unlockLinks();
            }
        );

    }

    function getSearchString(){
        return $("#search-text").val();
    }

    function lockLinks(){

        $("#search-loader").fadeIn();
        ICCS.lockLink("#search-name-button","");
        ICCS.lockLink("#search-quantity-button","");

    }

    function unlockLinks(){

        $("#search-loader").fadeOut(function(){
            ICCS.unlockLink("#search-name-button","Search By Name");
            ICCS.unlockLink("#search-quantity-button","Search By Amount");

        });

    }

    function mergeSelected(){

        if(ICCS.isLocked("#merge-button")){
            return;
        }

        ICCS.lockButton("#merge-button");
        $("#search-loader").fadeIn();



        var formData = $("#merge-form").serialize();
        console.log("Form data  "+formData);

        $.post("<@spring.url "/search/merge"/>",
                formData,
                function(data){
                    console.log("Done "+data);
                    searchName();
                }).complete(function() {
                    $("#search-loader").fadeOut(function(){
                        ICCS.unlockButton("#merge-button");
                    });
                });;

    }

</script>
    <div class="container">

        <h1> Type your search query </h1>
        <div id="search-control">
            <input type="text" id="search-text">
            <a id="search-name-button" class="search-button" href="#" onclick="searchName();return false;">Search By Name</a>
            <a id="search-quantity-button" class="search-button" href="#" onclick="searchQuantity();return false;">Search By Amount</a>
            <@iccs.loader id="search"/>
        </div>
        <div id="merge-control">
            <form id="merge-form">
                <p id="merge-ids">

                </p>
                <input style="display:none" id="merge-button" type="button" value="Merge" onclick="mergeSelected();"/>
            </form>

        </div>
        <div style="clear:both"/>
        <div id="search-results">
            <h3>Type a search term and press ENTER</h3>
        </div>
    </div>
</body>
</html>