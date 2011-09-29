<!-- freemarker macros have to be imported into a namespace.  We strongly
recommend sticking to 'spring' -->
<#import "spring.ftl" as spring/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"

<head>
    <title>Identity Representation</title>
    <link rel="stylesheet/less" type="text/css" href="<@spring.url "/static/css/main.less"/>">
    <script src="<@spring.url "/static/js/less-1.1.3.min.js" />" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/stardom.js" />" type="text/javascript"></script>
    <script src="<@spring.url "/static/js/jquery-1.6.1-min.js"/>" type="text/javascript"></script>
</head>

<body>
    <div class="container">
        <#include "ui/pagination.ftl"/>
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
            <#list identities as identityBean>
                <li class="identity clearfix">
                    <div class="identity">
                        <img src="<@spring.url "/static/images/identity.png"/>" alt="Identity" border="none"/>
                        <br/>
                        <p class="identity-uuid">${identityBean.identity.uuid}</p>
                    </div>
                    <div class="profiles">
                        <#list identityBean.identity.profiles as profile>
                            <#include "identity/profile.ftl"/>
                        </#list>
                    </div>
                    <div class="metrics">
                        <#include "identity/metrics.ftl"/>
                    </div>
                    <div class="ci">
                        ${identityBean.ci}
                    </div>
                </li>
            </#list>
        </ul>

        <#include "ui/pagination.ftl"/>
</body>
</html>