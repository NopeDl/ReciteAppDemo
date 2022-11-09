function editReset() {
    for (let x of all('.learn_page .highlight')) {
        x.setAttribute('contenteditable', false)
        x.className = 'highlight';
        x.onclick = null;
        x.style.userSelect = '';
    }
    for (let x of $('.learn_page footer li')) {
        x.classList.remove('choice');
    }
    $('.learn_page .text_box').setAttribute('contenteditable', false);
    $('.learn_page .text_box').classList.remove('del');
    $('.learn_page .title').classList.remove('canwrite');
    flag = false;
    flag1 = false;
    learn_flag_1 = true;
    learn_flag_2 = true;
}

//点击自定义
$('.zidingyi').onclick = () => {
    learnReset();
    $('.learn_page .footer_1').style.display = 'none';
    $('.learn_page .footer_2').style.display = 'block';
    
}

//点击编辑
$('.bianji').onclick = () => {
    editReset();
    $('.learn_page .text_box').setAttribute('contenteditable', true);
    $('.bianji').classList.add('choice');
    $('.learn_page .title').classList.add('canwrite');
}

//防止对已选中的文本进行多次挖空
let flag = true;
//防止退出选择模式后可选择节点
let flag1 = true;
//数组用来存放被选中的节点
let arr = [];

//点击挖空进入挖空模式
$('.wakong').onclick = () => {
    editReset()
    $('.wakong').classList.add('choice');
    flag = true;
    //当长按屏幕触屏结束时，选中文本 
    $('.learn_page .text_box').ontouchstart = () => {
        $('.learn_page .text_box').style.userSelect = '';
    }
    $('.learn_page .text_box').onmousedown = () => {
        $('.learn_page .text_box').style.userSelect = '';
    }
    $('.learn_page .text_box').ontouchend = wakong;
    $('.learn_page .text_box').onmouseup = wakong;
    function wakong() {
        //判断当前是否为挖空模式
        if ($('.wakong').classList.contains('choice')) {
            flag = true;
        } else {
            flag = false;
        }
        //获取当前选中的文本对象
        let txt = window.getSelection();
        //如果选中文本不为空且为挖空模式时

        if (txt.toString().length > 0 && flag) {
            //判断是否需要合并div
            let n = 0;
            // 获取当前selection对象下的range对象
            let range = txt.getRangeAt(0);
            // 创造id为merge的新节点并将选中文本放进去
            let newNode = document.createElement("div");
            newNode.setAttribute('class', 'highlight');
            newNode.setAttribute('id', 'merge');
            newNode.innerHTML = range.toString();
            //如果选中范围在div里面直接终止点击事件
            if (txt.anchorNode.parentNode === txt.focusNode.parentNode && txt.anchorNode.parentNode.className != 'text_box' && txt.focusNode.parentNode.className != 'text_box') {
                CancelHollowing(txt.anchorNode.parentNode, false);
                return;
            }
            if (txt.anchorNode.parentNode.className == 'container' || txt.focusNode.parentNode.className == 'container') {
                return;
            }
            //循环存储之前被选中的节点
            for (let x of arr) {
                //防止出现空节点
                if (!(x instanceof Node)) {
                    arr.remove(x);
                }//选中文本包含之前被选中的节点的全部
                else if (txt.containsNode(x, false)) {
                    continue;
                }//选中文本包含之前被选中的节点的一部分时，给之前被选中的节点添加id可标识
                else if (txt.containsNode(x, true)) {
                    x.setAttribute('id', 'merge');
                    n++;
                }
            }

            //将选中的文本区域在页面删除并插入新节点
            txt.deleteFromDocument();
            range.insertNode(newNode);
            //获取已标记的节点
            let div = $('#merge');

            // 当n大于0时，需要合并节点
            if (n > 0) {
                for (let i = 1; i < div.length; i++) {
                    // 将第二个节点到最后一个节点合并到第一个节点中，并且从数组和页面中删除
                    if (div.length > 1) {
                        div[0].innerHTML += div[i].innerHTML;
                    }
                    arr.remove(div[i]);
                    $('.learn_page .text_box').removeChild(div[i]);
                }
                // 移除id
                div[0].removeAttribute('id');
            } else {
                div.removeAttribute('id');
            }
            flag = false;

            // 将原数组清空，重新将选中节点添加进数组中
            arr = [];
            //清除空标签
            for(let x of all('.learn_page .highlight')){
                if(x.innerHTML == '')
                $('.learn_page .text_box').removeChild(x);
            }
            let len = all('.learn_page .highlight').length;
            for (let i = 0; i < len; i++) {
                let x = all('.learn_page .highlight')[i];
                //清除高亮标签后面的空文本
                function clean() {
                    if (x.nextSibling.textContent == '') {
                        $('.learn_page .text_box').removeChild(x.nextSibling);
                    }
                }
                clean();
                //如果两个标签相邻时合并
                if (x.nextSibling.className == 'highlight') {
                    x.innerHTML += all('.learn_page .highlight')[i + 1].innerHTML;
                    len--;
                    $('.learn_page .text_box').removeChild(all('.learn_page .highlight')[i + 1]);
                    clean();
                    console.log(all('.learn_page .highlight')[i + 1],x.nextSibling);
                    //如果合并后下一个标签还是相邻，就把标签合并
                    if(x.nextSibling.className == 'highlight'){
                        
                        x.innerHTML += all('.learn_page .highlight')[i + 1].innerHTML;
                        len--;
                        $('.learn_page .text_box').removeChild(all('.learn_page .highlight')[i + 1]);
                    }
                        
                    arr.push(x);
                } else {
                    arr.push(x);
                }

            }

        }
        //取消文本选择
        // $('.learn_page .text_box').style.userSelect = 'none';
    }
}
let mid = null;
//点击保存
$('.learn_page .finish').onclick = () => {

    let title1 = $('.learn_page .title').innerHTML;
    let info1 = $('.learn_page .text_box').innerHTML;
    let label1 = $('.learn_page .label').innerHTML;
    
    //标题和文本内容不能为空
    // if (title1 == '' || info1 == '') {
    //     $('.learn_page .popup_box').innerHTML = '标题和文本内容不能为空';
    //     $('.learn_page .popup').style.display = 'block';
    //     return;
    // }


    editReset();
    $('.learn_page .footer_1').style.display = 'block';
    $('.learn_page .footer_2').style.display = 'none';
    //如果是新建模板
    if (newTPFlag) {
        mid = all('.my_base li')[0].querySelector('.modleId').innerHTML;
    } else {
        mid = modleId.innerHTML;
    }
    let fal = true;

    // 标题一致就取消保存并提醒
    // Array.from(all('.my_base .title')).forEach((x,i) => {
    //     if (x.innerHTML == title1 && mid != all('.my_base li')[i].querySelector('.modleId').innerHTML) {
    //         $('.edit_page .popup_box').innerHTML = '标题不能与记忆库的模板重复';
    //         $('.edit_page .popup').style.display = 'block';
    //         fal = false;
    //         return;
    //     }
    // })

    if(fal){
        if(mStatus == '0'){
            if (newTPFlag) {
                all('.my_base li')[0].querySelector('.title').innerHTML = title1;
                all('.my_base li')[0].querySelector('.info').innerHTML = info1;
                all('.my_base li')[0].querySelector('.label span')[1].innerHTML = label1;
            } else {
                title.innerHTML = title1;
                info.innerHTML = info1;
                label.querySelectorAll('span')[1].innerHTML = label1;
            }
        }
        let poststr = '';
        let newinfo = info1.replace(/&nbsp;/g,'<空格>');
        console.log(newinfo);
        if(mStatus == 1){
            poststr = `context=${newinfo}&userId=${curr.userId}&modleTitle=${title1}&overWrite=0&modleLabel=${labelId1(label1)}&modleId=${mid}`
        }else{
            poststr = `context=${newinfo}&userId=${curr.userId}&modleTitle=${title1}&overWrite=1&modleLabel=${labelId1(label1)}&modleId=${mid}`
        }
        ajax(`http://8.134.104.234:8080/ReciteMemory/modle/MakeModle`, 'post', poststr, (str) => {
            let newstr = JSON.parse(str).msg;
            console.log(newstr);
            if(mStatus == 1){
                let modle = newstr.data.modle;
                newTPFlag = true;
                newTP(title1,info1,modle.modleId,label1,0,'未学习',true);
                mStatus = 0;
                $('.footer_nav li')[0].onclick();
            }
            xrcomTP();
        }, true);
    }

}

//点击关闭弹窗
$('.edit_page .popup').onclick = () => $('.edit_page .popup').style.display = 'none';
//阻止事件冒泡
$('.edit_page .popup .popup_box').onclick = (e) => e.stopPropagation();

//选择节点函数点击选中div中所有内容，点击取消挖空,参数e为选中节点，n为判断是否自动点击
function CancelHollowing(e, n) {
    //防止多次点击事件
    let flag2 = true;
    //获取当前选中的文本对象
    let txt = window.getSelection();
    let range = txt.getRangeAt(0);
    
    //将选中区域改成节点的文本内容
    // range.selectNodeContents(e);
    // //将选中节点从数组中删除
    // arr.remove(e);
    // //将选中节点的文本内容克隆一份
    let nonestr = document.createTextNode('');
    let text = document.createTextNode(range.toString());
    // //将选中节点区域扩大到整个节点
    // range.selectNode(e);
    // //将选中节点删除
    txt.deleteFromDocument();
    // //在原来的位置重新将文本插入
    range.insertNode(nonestr);
    let textArr = e.childNodes;
    
    //在原标签前面插入新节点
    function newNode(i) {
        let newNode = document.createElement("div");
        newNode.setAttribute('class', 'highlight');
        newNode.innerHTML = textArr[i].textContent;
        $('.learn_page .text_box').insertBefore(newNode,e);
    }

    if(textArr[0].textContent == ''){
        $('.learn_page .text_box').insertBefore(text,e);
        newNode(2);
        $('.learn_page .text_box').removeChild(e);
    }else{
        newNode(0);
        $('.learn_page .text_box').insertBefore(text,e);
        if(textArr[1].textContent == '' && textArr[2].textContent != ''){
            newNode(2);
        }
        $('.learn_page .text_box').removeChild(e);
    }
    
    
    //取消文本选择
    $('.learn_page .text_box').style.userSelect = 'none';
}

//点击返回记忆库
$('.edit_page .header_left').onclick = () => {
    $('.edit_page').style.left = '100%'
    learnReset()
}

let label_flag1 = true;
//点击出现下拉列表
$('.edit_page .label').onclick = () => {
    if (label_flag1) {
        $('.edit_page .label_menu').style.transform = 'scale(1)';
        label_flag1 = false;
    } else {
        $('.edit_page .label_menu').style.transform = 'scale(0)';
        label_flag1 = true;
    }
}

//事件委托，为li绑定事件
$('.edit_page .label_menu').onclick = (e) => {
    e.stopPropagation;
    $('.edit_page .label_cont').innerHTML = e.target.innerHTML;
}

