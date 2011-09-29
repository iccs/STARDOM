<div class="pagination clearfix">
    <#assign pages = pagination['pages'] />
    <#if pages?has_content>
        <ul>
            <li class="page">
            <#if selected != pagination['first']>
                <a href="<@spring.url "/identities/${pagination['first']}"/>">&laquo; First</a>
                <a href="<@spring.url "/identities/${selected-1}"/>">Prev</a>
            </#if>
            </li>
                <#list pages as page>
                    <li class="page <#if page == selected >selected</#if>" >
                        <a href="<@spring.url "/identities/${page}"/>">${page}</a>
                    </li>
                </#list>
            <#if selected != pagination['last']>
                <li class="page">
                    <a href="<@spring.url "/identities/${selected+1}"/>">Next</a>
                    <a href="<@spring.url "/identities/${pagination['last']}"/>">Last &raquo;</a>
                </li>
            </#if>

        </ul>
    <#else>
        Empty
    </#if>
</div>