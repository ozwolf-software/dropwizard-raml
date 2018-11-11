[#ftl auto_esc=false]

[#import "utility-tools.ftl" as utils/]

[#macro printDocumentation id item ]
<div id="${id}" class="container">
    <div class="row hover-highlight border-bottom mt-2 p-1">
        <h1>${item.title}</h1>
    </div>
    <div class="row p-3">
        ${item.contentHtml}
    </div>
</div>
[/#macro]