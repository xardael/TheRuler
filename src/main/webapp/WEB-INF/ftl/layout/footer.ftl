<#escape x as x?html>
<hr>

<footer>
    <p>&copy; Peter Gren 2013 | ${rc.getMessage("copyright")}</p>
</footer>

</div> <!-- /container -->


<!-- Modal to see validation resulsts
================================================== -->
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <h3 id="myModalLabel">${rc.getMessage("validationResult")}</h3>
    </div>
    <div class="modal-body">
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">${rc.getMessage("close")}</button>
    </div>
</div>

<!-- Modal to insert ruleref
================================================== -->
<div id="rulerefModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <h3 id="myModalLabel">${rc.getMessage("insertRuleref")}</h3>
    </div>
    <div class="modal-body">
        <form id="ruleSearchForm">
            <div class="input-append">
                <input type="hidden" value="${(gm.id)!}" name="grammarId" id="grammarId" >
                <input type="text" class="span3" id="ruleSearchText">
                <button type="submit" class="btn" id="ruleSearchButton"><i class="icon-search"></i></button>
            </div>
        </form>
        <ul class="unstyled" id="ruleList">
            <#if rules??>
                <#if rules?size != 0>
                    <#list rules as rule>
                        <li><buton class="btn btn-link insert">${(rule.id)!}</button></li>
                    </#list>
                </#if>
            </#if>
        </ul>
        <#if rules??>
            <#if rules?size == 0>
                <div class="alert" id="noRules">${rc.getMessage("noRules")}.</div>
            <#else>
                <div class="alert hidden" id="noResultAlert">${rc.getMessage("noResults")}.</div>
            </#if>
        </#if>

    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">${rc.getMessage("close")}</button>
    </div>
</div>

<!-- Javascript
================================================== -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="${rc.contextPath}/res/js/bootstrap.min.js"></script>
<script src="${rc.contextPath}/res/js/bootbox.min.js"></script>


<script type="text/javascript">
        
    // Rule collapser    
    var toggle = 0;
         
    // Get popover and tooltip working with attribute
    $(function () {
        $('body').popover({
            selector: '[data-toggle="popover"]'
        });

        $('body').tooltip({
            selector: 'a[rel="tooltip"], [data-toggle="tooltip"]'
        });
    });

    // Check new grammar emptiness
    $('[name="newGrammar"]').submit(function() {
        if ($.trim($('[name="name"]').val()).length == 0) {
            bootbox.alert("${rc.getMessage('emptyGrammarName')}.");
            return false;
        }
    });
    
    // Check new rule emptiness and existention
    $('[name="ruleAdd"]').submit(function() {
        if ($.trim($('[name="ruleId"]').val()).length == 0) {
            bootbox.alert("${rc.getMessage('emptyRuleName')}.");
            return false;
        } else {
            $.getJSON("${rc.contextPath}/ajax/ruleExists", { ruleId: $('input[name="ruleId"]').val(), grammarId: $('input[name="grammarId"]').val()}, function(result) {
                alert(result.exists);
                if(result.exists == "true") {
                    bootbox.alert("${rc.getMessage('ruleExists')}");
                    return false;
                }
                return false;
//                if (availability.available) {
//                    fieldValidated("name", { valid : true });
//                } else {
//                    fieldValidated("name", { valid : false,
//                        message : $('#name').val() + " is not available, try " + availability.suggestions });
//                }
            });
            return false;
            
            
            // debug
//            $a = $('input[name="ruleId"]').val();
//            $b = $('input[name="grammarId"]').val();
//            // Check if rule exists
//            $.ajax({
//                type: "POST",
//                url: "${rc.contextPath}/ajax/ruleExists",
//                data: { ruleId: $('input[name="ruleId"]').val(), grammarId: $('input[name="grammarId"]').val()},
//                dataType: "JSON"
//            }).always(function() {
//                alert(msg);
//                if (msg == 'true') {
//                    bootbox.alert("${rc.getMessage('ruleExists')}");
//                    return false;
//                }
//            });
        }
    });
       
    // Confirm deletion   
    $(".delete").click( function(e) {
        var href = $(this).attr('href');
        e.preventDefault();
                
        bootbox.dialog("${rc.getMessage('confirmDelete')}", [{
                "label" : "${rc.getMessage('cancel')}"
            }, {
                "label" : "${rc.getMessage('ok')}",
                "class" : "btn-primary",
                "callback": function() {
                    if (e.isDefaultPrevented()) {
                        window.location = href;
                    }
                }
            }
        ]);
                
        return false;
    });
            
    // Autmatic form field emptiness validation
    $('form.validate').submit(function() {
        var isFormValid = true;
                
        $("input.required", this).each(function(){ 
            if ($.trim($(this).val()).length == 0){
                $(this).closest('div.control-group').addClass('error');
                $(".help-inline", $(this).parent()).text('${rc.getMessage("fillEmpty")}');
                isFormValid = false;
            } else {
                $(this).closest('div.control-group').removeClass('error');
                $(".help-inline", $(this).parent()).text('');
            }
        });
                
        return isFormValid;
    });
            
    // Toggles rules collapse
    $('#collapseBtn').click(function() {
        if (toggle % 2 == 0) {
            $('i', this).removeClass('icon-chevron-down');
            $('i', this).addClass('icon-chevron-up');
        } else {
            $('i', this).removeClass('icon-chevron-up');
            $('i', this).addClass('icon-chevron-down');
        }
                
        toggle++;
        $(".mcollapse").collapse('toggle');
    });
       
    // Validete xml against XML Schema of SRGS via AJAX
    $('#validateXml').click(function() {
        var btn = $(this)
        btn.button('loading');
//        $.getJSON("${rc.contextPath}/ajax/ruleExists", { ruleId: $('input[name="ruleId"]').val(),
//                                                          grammarId: $('input[name="grammarId"]').val()}, function(result) {
            
//        });
                                                          
        $.ajax({
            type: "POST",
            url: "${rc.contextPath}/ajax/validateXml",
            data: { content: $('#content').val()},
            dataType: "JSON",
            timeout: 60000
        }).done(function(result) {
            btn.button('reset');
            if (result.valid == 'true') {
                $('#myModal .modal-body').html('<div class="alert alert-success"><strong>${rc.getMessage("wellDone")}!</strong> ${rc.getMessage("documentValid")}</div>');
            } else {
                $('#myModal .modal-body').html('<div class="alert alert-error"><strong>${rc.getMessage("error")}!</strong> ${rc.getMessage("documentNotValid")}.</div><p>' + msg + '</p>');
            }
            $('#myModal').modal('toggle');
        }).always(function(result) {
            alert(result.valid);
        });
    });
        
    // Displays windows with rulerefs to insert
    $('#insertRuleref').click(function () {
        $('#rulerefModal').modal('toggle');
    });
        
    // Call insertion after button click    
    $('.insert').click(function() {
        insert(this.innerHTML);
    });
       
    // Insert ruleref into textarea and close modal   
    function insert(value) {
        $('#content').insertAtCaret('<ruleref uri="#' + value + '"/>');
        $('#rulerefModal').modal('toggle');
    }
        
    // Insert text to textarea at cursor position    
    $.fn.insertAtCaret = function(myValue) {
        return this.each(function() {
            var me = this;
            if (document.selection) { // IE
                me.focus();
                sel = document.selection.createRange();
                sel.text = myValue;
                me.focus();
            } else if (me.selectionStart || me.selectionStart == '0') { // Real browsers
                var startPos = me.selectionStart, endPos = me.selectionEnd, scrollTop = me.scrollTop;
                me.value = me.value.substring(0, startPos) + myValue + me.value.substring(endPos, me.value.length);
                me.focus();
                me.selectionStart = startPos + myValue.length;
                me.selectionEnd = startPos + myValue.length;
                me.scrollTop = scrollTop;
            } else {
                me.value += myValue;
                me.focus();
            }
        });
    };
        
    // Get filtered rules via AJAX and display them into insert ruleref dialog 
    $('#ruleSearchForm').submit(function() {
        $.ajax({
            type: "POST",
            url: "${rc.contextPath}/ajax/findRules",
            data: { searchText: $('#ruleSearchText').val(), grammarId: $('#grammarId').val()},
            dataType: "JSON"
        }).done(function(data) {
            $('#ruleList').empty();
            if(data.length == 0) {
                $('#noResultAlert').removeClass('hidden');
            } else {
                $('#noResultAlert').addClass('hidden');
                $.each(data, function(index) {
                    $('#ruleList').append('<li><buton class="btn btn-link insert" onclick="insert(this.innerHTML);">' + data[index] + '</button></li>');
                });
            }
        });
            
        return false;
    });
          
</script>

</body>
</html>

</#escape>