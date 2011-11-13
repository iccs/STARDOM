<#import "../macro/iccs.ftl" as iccs/>

<div class="pagination clearfix">

    <div class="loader-holder">
        <@iccs.loader id="list"/>
    </div>
    <#assign pages = pagination['pages'] />
    <#if pages?has_content>
        <ul>
            <li class="page">
                <a class="page-link" href="#" title="<@spring.url "/ui/list/previous"/>">&laquo; Previous</a>
                <a class="page-link" href="#" title="<@spring.url "/ui/list/next"/>">&raquo; Next</a>
            </li>
            <li class="page">
            <#if selected != pagination['first']>
                <a class="page-link" href="#" title="<@spring.url "/ui/list/${pagination['first']}"/>">&laquo; First</a>
                <a class="page-link" href="#" title="<@spring.url "/ui/list/${selected-1}"/>">Prev</a>
            </#if>
            </li>
                <#list pages as page>
                    <li class="page <#if page == selected >selected</#if>" >
                        <a class="page-link" href="#" title="<@spring.url "/ui/list/${page}"/>">${page}</a>
                    </li    >
                </#list>
            <#if selected != pagination['last']>
                <li class="page">
                    <a class="page-link" href="#" title="<@spring.url "/ui/list/${selected+1}"/>">Next</a>
                    <a class="page-link" href="#" title="<@spring.url "/ui/list/${pagination['last']}"/>">Last &raquo;</a>
                </li>
            </#if>

        </ul>
    <#else>
        Empty
    </#if>
</div>