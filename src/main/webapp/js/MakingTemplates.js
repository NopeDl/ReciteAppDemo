let label_flag = true;
//点击出现下拉列表
$('.Making_page .label').onclick = () => {
    if(label_flag){
        $('.Making_page .label_menu').style.transform = 'scale(1)';
        label_flag = false;
    }else{
        $('.Making_page .label_menu').style.transform = 'scale(0)';
        label_flag = true;
    }  
}

//事件委托，为li绑定事件
$('.Making_page .label_menu').onclick = (e) => {
    e.stopPropagation;
    $('.Making_page .label_cont').innerHTML = e.target.innerHTML;
}