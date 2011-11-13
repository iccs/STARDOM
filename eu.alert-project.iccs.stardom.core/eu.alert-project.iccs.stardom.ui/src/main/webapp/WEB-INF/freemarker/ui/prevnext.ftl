<#import "../macro/iccs.ftl" as iccs/>

<div class="pagination clearfix">
    <div id="button-pages">
            <div class="loader-holder">
                <@iccs.loader id="list"/>
            </div>


            <a class="page-link previous" href="#" title="<@spring.url "/ui/list/previous"/>">&laquo; Previous</a>
            <span id="pages">&nbsp;</span>
            <a class="page-link next" href="#" title="<@spring.url "/ui/list/next"/>">Next&raquo;</a>
    </div>
</div>