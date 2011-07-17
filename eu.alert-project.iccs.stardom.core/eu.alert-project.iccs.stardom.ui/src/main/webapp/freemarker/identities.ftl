<!-- freemarker macros have to be imported into a namespace.  We strongly
recommend sticking to 'spring' -->
<#import "spring.ftl" as spring />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"

<head>
    <title>Identity Representation</title>
    <link rel="stylesheet/less" type="text/css" href="/css/main.less">
    <script src="/js/less-1.1.3.min.js" type="text/javascript"></script>
    <script src="/js/stardom.js" type="text/javascript"></script>
    <script src="/js/jquery-1.6.1-min.js" type="text/javascript"></script>
</head>

<body>
    <div class="container">
        <div class="header clearfix">
            <div class="identity">
                Identity
            </div>
            <div class="profiles">
                Profile
            </div>
            <div class="metrics">
                Metrics
            </div>
            <div class="ci">
                CI
            </div>
        </div>
        <ul class="identity-list">
            <#list identities as identity>
                <li class="identity clearfix">
                    <div class="identity">
                        <img src="/images/identity.png" alt="Identity" border="none"/>
                        <br/>
                        <p class="identity-uuid">${identity.uuid}</p>
                    </div>
                    <div class="profiles">
                        <#list identity.profiles as profile>
                            <#include "identity/profile.ftl"/>
                        </#list>
                    </div>
                    <div class="metrics">
                        <#include "identity/metrics.ftl"/>
                    </div>
                    <div class="ci">
                        25
                    </div>
                </li>
            </#list>
        </ul>

        <div class="pagination clearfix">
            <ul>
                <li class="page">
                <#if selected != pagination['first']>
                    <a href="<@spring.url "/ui/identities/${pagination['first']}"/>">&laquo; First</a>
                </#if>
                </li>
                <#list pagination['pages'] as page>
                    <li class="page <#if page == selected>selected</#if>" >
                        <a href="<@spring.url "/ui/identities/${page}"/>">#{page}</a>
                    </li>
                </#list>

                <#if selected != pagination['last']>
                    <li class="page"><a href="<@spring.url "/ui/identities/${pagination['last']}"/>">Last &raquo;</a></li>
                </#if>

            </ul>
        </div>
    </div>
</body>
</html>