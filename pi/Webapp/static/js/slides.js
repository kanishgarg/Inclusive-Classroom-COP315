
(function () {
	var $filesendbutton = document.getElementById('file-send-button');
	

    $('#file-1').change(function() {
        input = document.getElementById('file-1');
        file = input.files[0];
        $('#file-desc').text(file.name);
    });


})(jQuery);