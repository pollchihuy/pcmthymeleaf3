function funcModalsHandler(event)
{
    event.preventDefault(); //prevent default action
    var button = event.target
    var dataTitle = button.dataset.title
    var dataTarget = button.dataset.target
    var urlz = button.dataset.url
    var serverz = button.dataset.server
    $(dataTarget).on('show.bs.modal',function(){
        $.get(urlz, function (data) {
            let pattern = /Login/i;
            let result = data.match(pattern);
            try{
                if(result){
                    window.location = "/er";
                }else{
                    $(serverz).html(data);
                }
            }catch(r)
            {
                console.log('error '+r)
            }finally
            {
                $(dataTarget).find('.modal-title').text(dataTitle)
            }
        });
    })
}