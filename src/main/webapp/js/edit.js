function reset() {
    for (let x of btns) {
        x.classList.remove('choice');
    }
    $('.text_page').setAttribute('contenteditable', false);
    $('.text_page').classList.remove('del');
    flag = false;
    flag1 = false;
    $('.edit_page .header_right .name')[3].innerHTML = '保存';
}
let btns = $('.edit_page .header_right li');
//点击编辑
btns[0].onclick = () => {
    console.log(title);
    reset();
    $('.text_page').setAttribute('contenteditable', true);
    btns[0].classList.add('choice');

}

//防止对已选中的文本进行多次挖空
let flag = true;
//防止退出选择模式后可选择节点
let flag1 = true;
//数组用来存放被选中的节点
let arr = [];

//点击挖空进入挖空模式
btns[1].onclick = () => {
    reset();
    btns[1].classList.add('choice');
    flag = true;
    //当长按屏幕触屏结束时，选中文本 
    $('.text_page').onmouseup = (e) => {
        //判断当前是否为挖空模式
        if (btns[1].classList.contains('choice')) {
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
            //创造id为merge的新节点并将选中文本放进去
            let newNode = document.createElement("div");
            newNode.setAttribute('class', 'highlight');
            newNode.setAttribute('id', 'merge');
            newNode.innerHTML = range.toString();
            //如果选中范围在div里面直接终止点击事件
            console.log(txt.anchorNode.parentNode, txt.focusNode.parentNode);
            if (txt.anchorNode.parentNode === txt.focusNode.parentNode && txt.anchorNode.parentNode.className != 'text_page' && txt.focusNode.parentNode.className != 'text_page') {
                return;
            }
            if (txt.anchorNode.parentNode.className == 'text_box' || txt.focusNode.parentNode.className == 'text_box') {
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
                    $('.text_page').removeChild(div[i]);
                }
                // 移除id
                div[0].removeAttribute('id');
            } else {
                div.removeAttribute('id');
            }
            flag = false;
        }
        // 将原数组清空，重新将选中节点添加进数组中
        arr = [];
        for (let y of all('.text_page .highlight')) {
            arr.push(y);
        }
    }
}

//点击进入选择模式
btns[2].onclick = () => {
    reset();
    // 当页面有可选择的节点时
    if (document.querySelector('.text_page .highlight')) {
        btns[2].classList.add('choice');
        $('.text_page').classList.add('del');
        flag1 = true;
        for (let x of all('.text_page .highlight')) {
            x.addEventListener('click', (e) => {
                if (flag1)
                    CancelHollowing(e.target, false);
            });
        }
    }

}

//点击保存
btns[3].onclick = () => {
    let title1 = $('.edit_page .title_name').value;
    let info1 = $('.edit_page .text_page').innerHTML;
    let label1 = $('.edit_page .label_cont').innerHTML;
    let mid = null;
    //标题和文本内容不能为空
    if (title1 == '' || info1 == '') {
        $('.edit_page .popup_box').innerHTML = '标题和文本内容不能为空';
        $('.edit_page .popup').style.display = 'block';
        return;
    }


    reset();
    //如果是新建模板
    if (newTPFlag) {
        mid = all('.my_base li')[0].querySelector('.modleId').innerHTML;
    } else {
        mid = modleId.innerHTML;
    }
    let fal = true;
    // 标题一致就取消保存并提醒
    Array.from(all('.my_base .title')).forEach((x,i) => {
        if (x.innerHTML == title1 && mid != all('.my_base li')[i].querySelector('.modleId').innerHTML) {
            $('.edit_page .popup_box').innerHTML = '标题不能与记忆库的模板重复';
            $('.edit_page .popup').style.display = 'block';
            fal = false;
            return;
        }
    })

    if(fal){
        if (newTPFlag) {
            all('.my_base li')[0].querySelector('.title').innerHTML = title1;
            all('.my_base li')[0].querySelector('.info').innerHTML = info1;
            console.log(all('.my_base li')[0]);
            all('.my_base li')[0].querySelector('.label span')[1].innerHTML = label1;
        } else {
            title.innerHTML = title1;
            info.innerHTML = info1;
            label.querySelectorAll('span')[1].innerHTML = label1;
        }
        let poststr = '';
        let newinfo = info1.replace(/&nbsp;&nbsp;&nbsp;&nbsp;/g,'<缩进>');

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
                newTP(title1,info1,modle.modleId,label1,true);
                $('.collection_base ul').removeChild(modleId.parentNode.parentNode);
                mStatus = 0;
                $('.footer_nav li')[0].onclick();
            }
            
        }, true);
        btns[3].classList.add('choice');
        $('.edit_page .header_right .name')[3].innerHTML = '已保存';
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
    range.selectNodeContents(e);
    //将选中节点从数组中删除
    arr.remove(e);
    //将选中节点的文本内容克隆一份
    let str = range.cloneContents();
    //将选中节点区域扩大到整个节点
    range.selectNode(e);
    //将选中节点删除
    txt.deleteFromDocument();
    //在原来的位置重新将文本插入
    range.insertNode(str);
}

//点击返回记忆库
$('.edit_page .header_left').onclick = () => {
    $('.edit_page').style.left = '100%'
    reset();
}

let label_flag1 = true;
//点击出现下拉列表
$('.edit_page .label').onclick = () => {
    if(label_flag1){
        $('.edit_page .label_menu').style.transform = 'scale(1)';
        label_flag1 = false;
    }else{
        $('.edit_page .label_menu').style.transform = 'scale(0)';
        label_flag1 = true;
    }  
}

//事件委托，为li绑定事件
$('.edit_page .label_menu').onclick = (e) => {
    e.stopPropagation;
    $('.edit_page .label_cont').innerHTML = e.target.innerHTML;
}

