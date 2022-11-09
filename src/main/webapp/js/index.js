let curr = getData('current_user');

//登录拦截
if (curr.length == 0) {
    location.href = './login.html';
}

//今日计划和记忆库切换
for (let i = 0; i < $('.hb_btn').length; i++) {
    $('.hb_btn')[i].onclick = () => {
        let x = -100 * i;
        $('.container_scroll').style.left = `${x}vw`;
        for (let k of $('.hb_btn')) {
            k.classList.remove('btn_line')
        }
        $('.hb_btn')[i].classList.add('btn_line')
        if (i == 0) {
            let fxnum = 0;
            for (let x of all('.my_base .learning')) {
                if (x.innerText == '复习中')
                    fxnum++;
            }
            $('.numOfArticles').innerHTML = `${fxnum} 篇`;
            $('.icon_btn').style.display = 'none';
            $('.base_btn').querySelector('span').innerText = '记忆库';
        } else {
            $('.icon_btn').style.display = 'block';
            if ($('.collection_base').classList.contains('clip_path_change1')) {
                $('.base_btn').querySelector('span').innerText = '收藏库';
            } else {
                $('.base_btn').querySelector('span').innerText = '个人库';
            }

        }
    }
}
//排行榜判断
let judge = true;
//底部导航栏
for (let i = 0; i < $('.footer_nav li').length; i++) {
    $('.footer_nav li')[i].onclick = () => {
        for (let x of $('.footer_nav li'))
            x.style.color = '#9fa7ba'
        $('.footer_nav li')[i].style.color = '#5c4eaf';
        pageReset(i);
        if (i == 0) {
            $('.memory_base header').classList.remove('scroll_top');
        }
        if (i == 1) {
            $('.pk_page .pk_footer').classList.remove('scroll_top');
        }
        if (i == 3) {
            $('.community header').classList.remove('scroll_top');
            $('.community header .label li')[0].onclick();
        }
        if (i != 1 && !judge) {
            laqu.onclick()
        }
        if (i != 2) {
            $('.Making_page .loading').style.display = 'none';
        }

        $('.main_page')[i].classList.remove('scroll_top');

    }
}
$('.footer_nav li')[0].onclick();

function pageReset(i) {
    $('.memory_base header').classList.add('scroll_top');
    $('.pk_page .pk_footer').classList.add('scroll_top');
    $('.community header').classList.add('scroll_top');
    for (let x of $('.main_page')) {
        x.classList.add('scroll_top');
    }
    if (i == 0) {
        resetbase();
        TP();
    }

}
// 刷新仓库
function resetbase() {
    if (document.querySelector('.my_base li')) {
        for (let x of all('.my_base li'))
            x.classList.add('baseLis_fadeIn');
        $('.base_none_1').style.display = 'none';
    } else {
        $('.base_none_1').style.display = 'block';
    }
    if (document.querySelector('.collection_base li')) {
        for (let x of all('.collection_base li'))
            x.classList.add('baseLis_fadeIn');
        $('.base_none_2').style.display = 'none';
    } else {
        $('.base_none_2').style.display = 'block';
    }
}
//模板标题
var title = null;
//模板内容
var info = null;
//模板id
var modleId = null;
//模板标签
var label = null;
var flag_learn = false;
//是否为收藏
var mStatus = 0;
//是否为复习
var flag_review = false;

//为模板添加事件
function TP() {
    Array.from(all('.tp_inner')).forEach((x, i) => {
        x.ontouchstart = (e) => {
            let time1 = new Date().getSeconds();
            let l = 0;
            e.stopPropagation();
            let disX = e.changedTouches[0].clientX;
            // 滑动模板出现按钮
            x.addEventListener('touchmove', function(e) {
                    l = e.changedTouches[0].clientX - disX;
                })
                //如果没有滑动则进入学习页面
            x.parentNode.ontouchend = () => {
                    let time2 = new Date().getSeconds();
                    if (l == 0) {
                        if (Math.abs(time1 - time2) < 1 && !x.parentNode.classList.contains('baseLis_del')) {
                            title = all('.tp_inner .title')[i];
                            info = all('.tp_inner .info')[i];
                            modleId = all('.tp_inner .modleId')[i];
                            label = all('.tp_inner .label')[i];
                            $('.learn_page .title').innerHTML = title.innerHTML;
                            $('.learn_page .text_box').innerHTML = info.innerHTML;
                            $('.learn_page .label').innerHTML = all('.tp_inner .label')[i].querySelectorAll('span')[1].innerHTML;
                            $('.learn_page').style.left = '0';
                            $('.learn_page header').style.left = '0';
                            $('.learn_page header').style.opacity = '1';
                            flag_learn = true;
                        } else {
                            let delnum = 0;
                            $('.ht_1').style.display = 'none';
                            $('.ht_2').style.display = 'flex';
                            $('.header_bottom').style.display = 'none';
                            $('.memory_base .container').style.marginTop = '14vw';
                            x.parentNode.querySelector('.select').classList.toggle('selected');
                            for (let k of x.parentNode.parentNode.querySelectorAll('li')) {
                                k.classList.add('baseLis_del');
                                if (k.querySelector('.select').classList.contains('selected')) {
                                    delnum++;
                                }
                            }
                            $('.delnum').innerHTML = `共${delnum}项`
                        }
                    }


                }
                //点击编辑进入编辑页面
                // all('.template_btn .edit')[i].onclick = (e) => {
                //     title = all('.tp_inner .title')[i];
                //     info = all('.tp_inner .info')[i];
                //     modleId = all('.tp_inner .modleId')[i];
                //     label = all('.tp_inner .label')[i];
                //     console.log(label);
                //     if (all('.memory_base li')[i].parentNode.parentNode.classList.contains('collection_base'))
                //         mStatus = 1;
                //     $('.edit_page .title_name').value = all('.tp_inner .title')[i].innerHTML;
                //     $('.edit_page .text_page').innerHTML = all('.tp_inner .info')[i].innerHTML;
                //     $('.edit_page .label_cont').innerHTML = all('.tp_inner .label')[i].querySelectorAll('span')[1].innerHTML;
                //     $('.edit_page').style.left = '0';
                //     e.stopPropagation();
                // }

            // //点击删除模板
            // all('.template_btn .del')[i].onclick = (e) => {

            //     x.parentNode.classList.add('baseLis_del');
            //     e.stopPropagation();
            //     if (all('.my_base li').length == all('.my_base .baseLis_del').length)
            //         $('.base_none_1').style.display = 'block';
            //     if (all('.collection_base li').length == all('.collection_base .baseLis_del').length)
            //         $('.base_none_2').style.display = 'block';
            //     let modleId = all('.modleId')[i].innerHTML;
            //     if (i < all('.my_base li').length) {
            //         ajax(`http://8.134.104.234:8080/ReciteMemory/modle/deleteModle`, 'post', `modleId=${modleId}`, (str) => {
            //             let newstr = JSON.parse(str).msg;
            //             console.log(newstr);
            //             x.parentNode.addEventListener('animationend', () => {
            //                 $('.my_base ul').removeChild(x.parentNode);
            //             })
            //         }, true)
            //     } else {

            //         ajax(`http://8.134.104.234:8080/ReciteMemory/modle/CancelCollet?userId=${curr.userId}&modleId=${all('.tp_inner')[i].querySelector('.modleId').innerHTML}&mStatus=0`, 'get', '', (str) => {
            //             let newstr = JSON.parse(str).msg;
            //             console.log(newstr);
            //             x.parentNode.addEventListener('animationend', () => {
            //                 $('.collection_base ul').removeChild(x.parentNode);
            //             })
            //         }, true)
            //     }
            // }
        }
    });
}
TP();


// 点击切换收藏
let timer = null;
$('.icon_btn').onclick = (e) => {
    e.stopPropagation();
    if (!document.querySelector('.collection_base li'))
        $('.base_none_2').style.display = 'block';

    timer = null;
    if (document.querySelector('.my_base li')) {
        for (let x of all('.my_base li'))
            x.classList.add('baseLis_fadeIn');
        $('.base_none_1').style.display = 'none';
    } else {
        $('.base_none_1').style.display = 'block';
    }
    if (document.querySelector('.collection_base li')) {
        for (let x of all('.collection_base li'))
            x.classList.add('baseLis_fadeIn');
        $('.base_none_2').style.display = 'none';
    } else {
        $('.base_none_2').style.display = 'block';
    }
    TP();
    if ($('.base_btn').querySelector('span').innerText == '个人库') {
        $('.base_btn').querySelector('span').innerText = '收藏库';
    } else {
        $('.base_btn').querySelector('span').innerText = '个人库';
    }
    $('.collection_base').classList.toggle('clip_path_change1');
    $('.my_base').classList.toggle('clip_path_change2');
    // $('.icon_btn').classList.add('change_ani');
    $('.icon_btn').addEventListener('animationend', () => {
        // $('.icon_btn').classList.remove('change_ani');
        for (let x of all('.collection_base li')) {
            x.classList.add('baseLis_fadeIn');
        }

    })
}


//点击取消删除
$('.ht_2 .header_left').onclick = () => {
    $('.ht_1').style.display = 'flex';
    $('.ht_2').style.display = 'none';
    $('.header_bottom').style.display = 'flex';
    $('.memory_base .container').style.marginTop = '24.8vw';
    for (let k of all('.me_base li')) {
        k.querySelector('.select').classList.remove('selected');
        k.classList.remove('baseLis_del');
    }
    resetbase()
}

//点击删除模板
$('.ht_2 .header_right').onclick = () => {
    //删除模板
    for (let x of all('.my_base li')) {
        if (x.querySelector('.select').classList.contains('selected')) {
            ajax(`http://8.134.104.234:8080/ReciteMemory/modle/deleteModle`, 'post', `userId=${curr.userId}&modleId=${x.querySelector('.modleId').innerHTML}`, (str) => {
                let newstr = JSON.parse(str).msg;
                console.log(newstr);
                if (newstr.content == '删除成功') {
                    x.querySelector('.select').classList.remove('selected');
                    x.classList.add('baseLis_del2');
                    x.addEventListener('animationend', () => {
                        $('.my_base ul').removeChild(x);
                    })
                } else {
                    alert('删除模板失败');
                }
            }, true)

        }
    }
    //取消收藏
    for (let x of all('.collection_base li')) {
        if (x.querySelector('.select').classList.contains('selected')) {
            ajax(`http://8.134.104.234:8080/ReciteMemory/modle/Collection`, 'post', `userId=${curr.userId}&modleId=${x.querySelector('.modleId').innerHTML}&mStatus=0`, (str) => {
                let newstr = JSON.parse(str).msg;
                console.log(newstr);
                if (newstr.content == '取消收藏成功') {
                    x.classList.add('baseLis_del2');
                    x.addEventListener('animationend', () => {
                        $('.collection_base ul').removeChild(x);
                    })
                } else {
                    alert('取消收藏失败');
                }

            }, true)

        }
    }
    $('.delnum').innerHTML = `共0项`

}

//点击出现打卡页面
$('.ht_1 .header_left').onclick = () => {
    $('.calendar_page').style.left = '0';
}



//点击复习列表进入可移除复习列表页面
$(".review_list").addEventListener('click', () => {
    $(".removeRecite").classList.remove("scroll_top");
    $(".removeRecite .headBox").classList.remove("scroll_top");
    //复习列表渲染
    $('.reciteCon ul').innerHTML = '';
    for (let x of all('.my_base li')) {
        if (x.querySelector('.learning span').innerHTML == '复习中') {
            $('.reciteCon ul').innerHTML += `<li>
                                                <div class="modleId">${x.querySelector('.modleId').innerHTML}</div>
                                                <span class="title ellipsis">${x.querySelector('.title').innerHTML}</span>
                                                <span class="goReview">移除</span>
                                            </li>`
        }
    }
    //点击删除复习
    for (let k of all('.reciteCon li')) {
        let goReview = k.querySelector('.goReview');
        goReview.onclick = () => {
            ajax(`http://8.134.104.234:8080/ReciteMemory/review/RemoveFromPlan?userId=${curr.userId}&modleId=${k.querySelector('.modleId').innerHTML}&studyStatus=已学习`, 'get', '', (str) => {
                let newstr = JSON.parse(str).msg;
                console.log(newstr);
                //将记忆库对应的模板改成已学习
                for (let x of all('.my_base li')) {
                    if (k.querySelector('.modleId').innerHTML == x.querySelector('.modleId').innerHTML) {
                        x.querySelector('.learning span').innerHTML = '已学习';
                        x.querySelector('.learning').className = 'learning learned';
                        break;
                    }
                }
                for (let i = 0; i < 8; i++) {
                    for (let j = 0; j < ModlesOfPeriod[i].length; j++) {
                        if (ModlesOfPeriod[i][j].modleId == k.querySelector('.modleId').innerHTML) {
                            let removearr = ModlesOfPeriod[i];
                            Array.remove(removearr[j]);
                            break;
                        }
                    }
                }
                modle_Period();
                //删除复习周期中的li
                k.classList.add('review_del');
            }, true)
        }
    }
})

//点击移除复习列表的返回增添scroll_top
$(".removeRecite .back").addEventListener('click', () => {
    $(".removeRecite").classList.add("scroll_top");
    $(".removeRecite .headBox").classList.add("scroll_top");
    //修改复习篇数
    let fxnum = 0;
    for (let x of all('.my_base .learning')) {
        if (x.innerText == '复习中')
            fxnum++;
    }
    $('.numOfArticles').innerHTML = `${fxnum} 篇`;
})


let ModlesOfPeriod = null;
//获取复习周期模板
ajax(`http://8.134.104.234:8080/ReciteMemory/review/GetPeriodModle?userId=${curr.userId}`, 'get', '', (str) => {
    let newstr = JSON.parse(str).msg;
    console.log(newstr);
    ModlesOfPeriod = newstr.data.ModlesOfPeriod;
    console.log(ModlesOfPeriod);
    modle_Period();
}, true)

//渲染复习周期
function modle_Period() {
    let ulIndex = 0;
    let num = 0;
    for (let i = 0; i < 8; i++) {
        if (ModlesOfPeriod[i].length != 0) {
            $('.review_cycle').innerHTML += `<div class="cycle_1">
                                                <div class="cycle_title">
                                                    <span>复 习 周 期 ${digitChange(i)}</span>
                                                </div>
                                                <ul>
                                                </ul>
                                            </div>`
            for (let j = 0; j < ModlesOfPeriod[i].length; j++) {
                all('.review_cycle .cycle_1 ul')[ulIndex].innerHTML += `<li>
                                                                            <div class="modleId">${ModlesOfPeriod[i][j].modleId}</div>
                                                                            <span class="title ellipsis">${ModlesOfPeriod[i][j].modleTitle}</span> 
                                                                            <span class="goReview">去复习</span>
                                                                        </li>`
                num++;
            }
            ulIndex++;
        }
    }
    ;
    $('.today_review .sum').innerHTML = parseInt($('.today_review .cur').innerHTML) + num;
    if (num > 0) {
        $('.now_line').style.width = $('.today_review .cur').innerHTML / $('.today_review .sum').innerHTML * $('.review_line').offsetWidth + 'px';
    } else {
        $('.now_line').style.width = 0 + 'px';
    }
    if ($('.today_review .cur').innerHTML == $('.today_review .sum').innerHTML && $('.today_review .sum').innerHTML != 0) {
        $('.today_review .icon-xianshi_xuanze').style.backgroundColor = '#5f4fb8';
    }
    //点击去复习进入学习页面
    for (let x of all('.review_cycle li')) {
        x.querySelector('.goReview').onclick = () => {
            for (let k of all('.my_base li')) {
                if (k.querySelector('.modleId').innerHTML == x.querySelector('.modleId').innerHTML) {
                    title = k.querySelector('.title');
                    info = k.querySelector('.info');
                    modleId = k.querySelector('.modleId');
                    label = k.querySelector('.label');
                    $('.learn_page .title').innerHTML = title.innerHTML;
                    $('.learn_page .text_box').innerHTML = info.innerHTML;
                    $('.learn_page .label').innerHTML = k.querySelectorAll('.label span')[1].innerHTML;
                    $('.learn_page').style.left = '0';
                    $('.learn_page header').style.left = '0';
                    $('.learn_page header').style.opacity = '1';
                    flag_review = true;
                }
            }
        }
    }


}

console.log(1 / 2 * $('.review_line').offsetWidth);
//数字转换成文字
function digitChange(i) {
    let arr = ['一', '二', '三', '四', '五', '六', '七', '八'];
    return arr[i];
}