<div class="profile profile-${profile.id}">
    <ul>
        <li class="name"><label>Name</label><#if profile.name??>${profile.name}</#if></li>
        <li class="lastname"><label>Last Name</label><#if profile.lastname??>${profile.lastname}</#if></li>
        <li class="username"><label>Username</label><#if profile.username??>${profile.username}</#if></li>
        <li class="email"><label>E-Mail</label><#if profile.email??>${profile.email}</#if></li>
        <li class="soruce"><label>Source</label>${profile.source}</li>
    </ul>
</div>