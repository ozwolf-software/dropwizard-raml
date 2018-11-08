[#ftl]

[#import "utility-tools.ftl" as utils/]

[#macro printDocumentation item ]
[#assign documentId = utils.makeId(item.title)/]
<div class="row hover-highlight border-bottom mt-2 p-1" onclick="toggleVisibility('#documentation-${documentId}')">
    <h1>${item.title}</h1>
</div>
<div id="documentation-${documentId}" class="invisible d-none p-3">
    ${item.contentHtml?no_esc}
</div>
[/#macro]