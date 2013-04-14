
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
    
    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="${rc.contextPath}/res/js/bootstrap.min.js"></script>
    <script src="${rc.contextPath}/res/js/bootbox.min.js"></script>
    
    
     <script type="text/javascript">
        
        var toggle = 0;
         
        $(document).ready(function() {
            //bootbox.confirm("Are you sure?", function(result) {
            //Example.show("Confirm result: "+result);
            
            $('#content').each(function() {
                $this = $(this);
                $this.data('defaultval', $this.val());
            });
            
            showCancelButton();
        }); 
            
            //get popover working with attribute
            $(function () {
                $('body').popover({
                    selector: '[data-toggle="popover"]'
                });

                $('body').tooltip({
                    selector: 'a[rel="tooltip"], [data-toggle="tooltip"]'
                });
            });
            
            // Validate form fields in install form 
            $("#AinstallForm").blur(function(){
                alert("a");
                var isFormValid = true;
                $("#installForm .required input:text").each(function(){ 
                    if ($.trim($(this).val()).length == 0){
                        $(this).parent().addClass("error");
                        isFormValid = false;
                    } else {
                        $(this).parent().removeClass("error");
                    }
                });
                if (!isFormValid) alert("Please fill in all the required fields (highlighted in red)");
                return isFormValid;
            });
            
            $('[name="newGrammar"]').submit(function() {
                if ($.trim($('[name="name"]').val()).length == 0) {
                    bootbox.alert("${rc.getMessage('emptyGrammarName')}.");
                    return false;
                }
            });
            
            $('[name="ruleAdd"]').submit(function() {
                if ($.trim($('[name="ruleId"]').val()).length == 0) {
                    bootbox.alert("${rc.getMessage('emptyRuleName')}.");
                    return false;
                }
            });
            
            $(".noempty").closest("form").submit(function() {
                if ($.trim($('[name="name"]').val()).length == 0) {
                    bootbox.alert("${rc.getMessage('emptyGrammarName')}.");
                    return false;
                }
            });
            
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
            
            $(".discard").click( function(e) {
                if ($('#content').val() != $('#content').data('defaultval')) {
                    bootbox.dialog("${rc.getMessage('confirmDiscard')}", [{
                        "label" : "${rc.getMessage('cancel')}"
                        }, {
                            "label" : "${rc.getMessage('ok')}",
                            "class" : "btn-primary",
                            "callback": function() {
                                $('#content').val($('#content').data('defaultval'));
                            }
                        }
                    ]);
                    
                }
            });
            
            $("#content").keyup(function() {
                showCancelButton();
            });
            
            $("#content").change(function() {
                showCancelButton();
            });
            
            function showCancelButton() {
                if ($('#content').val() != $('#content').data('defaultval')) {
                    $('.discard').removeAttr("disabled");   
                }
            }
            
            $('#AinstallForm').submit(function() {
                if ($.trim($("input:first").val()).length == 0) {
                    $("input:first").parent().addClass("error");
                    $("#inputHost").attr('data-content','blah blah').attr('title','Chyba').popover('show');
                    $("#test").addClass("error");
                    $("#test span").removeClass("hidden");
                    $("#error").removeClass("hidden");
                    return false;
                }
                $("span").text("Not valid!").show().fadeOut(1000);
                return false;
                
                return false;
            });
            
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
            
        $('#validateXml').click(function() {
            var btn = $(this)
            btn.button('loading');
            $.ajax({
                type: "POST",
                url: "${rc.contextPath}/ajax/validateXml",
                data: { content: $('#content').val()}
                }).done(function( msg ) {
                    btn.button('reset');
                    if (msg == 'true') {
                        $('#myModal .modal-body').html('<div class="alert alert-success"><strong>${rc.getMessage("wellDone")}!</strong> ${rc.getMessage("documentValid")}</div>');
                    } else {
                        $('#myModal .modal-body').html('<div class="alert alert-error"><strong>${rc.getMessage("error")}!</strong> ${rc.getMessage("documentNotValid")}.</div><p>' + msg + '</p>');
                    }
                    $('#myModal').modal('toggle');
            });
        });
        
        /*
         * Displays windows with rulerefs to insert
         */
        $('#insertRuleref').click(function () {
            $('#rulerefModal').modal('toggle');
        });
        
        
        $('.insert').click(function() {
           insert(this.innerHTML);
        });
        
        function insert(value) {
           $('#content').insertAtCaret('<ruleref uri="#' + value + '"/>');
           $('#rulerefModal').modal('toggle');
        }
        
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
        
        /*
         * Get filtered rules and display them into insert ruleref dialog
         */
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
