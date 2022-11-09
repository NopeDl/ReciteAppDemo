let ws;
let answerArr = []; //存放当前答案
let UserSelectArr = []; //存放当前用户选择的答案是否正确
let selectArr = []; //下面选项的答案
let again_count = 0;

let endAnswerArr = [];//存放所有答案





function ConnectionClicked() {
    try {
        ws = new WebSocket(`ws://8.134.104.234:8080/ReciteMemory/PK/${curr.userId}/${modle_id}/${difficult}`) //连接服务器
        ws.onopen = function (event) {
            console.log("已经与服务器建立了连接当前连接状态：" + this.readyState);
        };

        let msgCount = 0; //接收信息的次数
        let timeLimits_record;
        let timeCount = 0;
        ws.onmessage = function (event) {
            console.log("接收到服务器发送的数据：" + event.data + "haha" + msgCount);
            msgCount++;
            if (event.data) {
                let res = JSON.parse(event.data).socketMsg.datas;
                console.log(res);

                //当连接成功后发送START开始游戏
                if (res.CONNECTION && msgCount == 1) {
                    this.send("START");
                    console.log("已经向服务器发送START信号！");
                }

                //当匹配成功后获取双方信息渲染，并且发送Ready开始答题
                if (res.MATCH_SUCCESS) {
                    timeCount = res.timeLimits - 2;
                    $(".innercir").innerHTML = timeCount;
                    //每轮pk开始前置空答案
                    $(".show_answer .answer").innerHTML = ``;
                    $(".show_answer .mine_an").innerHTML = ``;
                    //刷新信息
                    Refresh_id(res.enemyInf.nickName);
                    Refresh_img(res.enemyInf);
                    Refresh_Template(res.context);
                    ws.send("READY");
                    console.log("已经向服务器发送Ready信号！");
                }

                //判断匹配是否成功，成功后渲染挖空内容和答题的答案,当isReady为true即可进入pk界面
                if (res.isReady) {
                    $('.newwaitPK .back_bock').style.display = 'none';
                    animate_success();
                    console.log("我已经准备好啦！");
                    setTimeout(() => {
                        timeLimits_record = setInterval(() => {
                            console.log(timeCount);
                            $(".innercir").innerHTML = timeCount--;
                        }, 1000);
                    }, 1000);

                }

                //刷新血条
                if (res.hpInf) {
                    Refresh_blook(res.hpInf);
                }


                //发送AGAIN之后再刷新模板
                if (res.OPERATE) {
                    //刷新模板
                    animate($('.enterPk .text_box'), 0, $('.enterPk .text_box').scrollTop)
                    Refresh_Template(res.digedContent);

                }

                if (res.ENEMY_EXIT && !res.MATCH_END) {
                    $('.runle').style.display = 'block';

                }

                //比赛结束之后
                if (res.MATCH_END) {
                    clearInterval(timeLimits_record);
                    //渲染答案
                    for (let x of endAnswerArr) {
                        $(".show_answer .answer").innerHTML += `<div class="an">${x}</div>`;
                    }
                    //渲染当前用户的答案对错

                    for (let i = 0; i < UserSelectArr.length; i++) {
                        if (UserSelectArr[i] == "right") {
                            $(".show_answer .mine_an").innerHTML += `<div class="dui">√</div>`;
                        } else {
                            $(".show_answer .mine_an").innerHTML += `<div class="cuo">×</div>`;
                        }
                    }
                    if (UserSelectArr.length < endAnswerArr.length) {
                        for (let j = 0; j < endAnswerArr.length - UserSelectArr.length; j++) {
                            $(".show_answer .mine_an").innerHTML += `<div class="cuo">?</div>`;
                        }
                    }
                    endAnswerArr = [];
                    $('.runle').style.display = 'none';
                    ws.onclose = wsonclose(true, ws);
                }



                if (msgCount == 1) {
                    ws.onclose = wsonclose(false, ws);
                }

                //渲染对方答案对错
                if (res.records) {
                    RefreshXing(res.winnerId);
                    RefreshWin_Lose(res.winnerId);

                    for (let x of res.records) {
                        if (x.userId != curr.userId) {
                            let other_answer = x.answersRecord;
                            console.log(other_answer);
                            $(".show_answer .other_an").innerHTML = ``;

                            for (let i = 0; i < other_answer.length; i++) {
                                if (other_answer[i].right) {
                                    $(".show_answer .other_an").innerHTML += `
                                    <div class="dui">√</div>
                                    `;
                                } else {
                                    $(".show_answer .other_an").innerHTML += `
                                        <div class="cuo">×</div>`;
                                }
                                if (i == endAnswerArr.length - 1) {
                                    break;
                                }
                            }
                            if (other_answer.length < endAnswerArr.length) {
                                for (let j = 0; j < endAnswerArr.length - other_answer.length; j++) {
                                    $(".show_answer .other_an").innerHTML += `
                                    <div class="cuo">?</div>`;
                                }
                            }
                        }
                    }

                }
            }
        };

        //正在匹配关闭页面
        $('.newwaitPK .back_bock').onclick = () => {
            $('.img_box img').style.opacity = '0';
            $('.pk_end').style.display = 'none';
            $('.enterPk').classList.add('appear');
            $('.img_box').classList.remove("xz");
            $('.img_box').classList.add("disappear_xz");
            $('.img_box').classList.remove("appear");
            $('.newwaitPK .mine').classList.add('disappearup');
            $('.newwaitPK .other').classList.add('disappearbottom');
            setTimeout(() => {
                $('.newwaitPK').style.display = 'none';
                $('.newwaitPK .mine').classList.remove('disappearup')
                $('.newwaitPK .other').classList.remove('disappearbottom')
                $('.img_box').classList.remove("disappear_xz");

            }, 2000);
            console.log(ws.close());
            clearInterval(timeLimits_record);
        }

        //pk中关闭页面
        $('.enterPk .head_nav_pk .back').onclick = () => {
            $('.enterPk .head_nav_pk').classList.add('disappear');
            $('.enterPk .head_nav_pk').classList.add('animated');
            $('.enterPk .pk_blood .mine').classList.add('disLeft');
            $('.enterPk .pk_blood .mine').classList.add('animated');
            // $('.enterPk .pk_blood .enter_vs').classList.add('disappear');
            // $('.enterPk .pk_blood .enter_vs').classList.add('animated');
            $('.enterPk .pk_blood .time').classList.add('disappear');
            $('.enterPk .pk_blood .time').classList.add('animated');
            $('.enterPk .pk_blood .other').classList.add('disRight');
            $('.enterPk .pk_blood .other').classList.add('animated');
            $('.enterPk .text_box').classList.add('animated');
            $('.enterPk .text_box').classList.add('disappear');
            $('.enterPk .option').classList.add('animated');
            $('.enterPk .option').classList.add('disappear');
            $('.runle').style.display = 'none';
            setTimeout(function () {
                $('.enterPk').style.display = 'none';
                $('.pk_end').style.display = 'none';
                $('.enterPk .head_nav_pk').classList.remove('disappear');
                $('.enterPk .pk_blood .mine').classList.remove('disLeft');
                $('.enterPk .pk_blood .time').classList.remove('disappear');
                $('.enterPk .pk_blood .other').classList.remove('disRight');
                $('.enterPk .text_box').classList.remove('disappear');
                $('.enterPk .option').classList.remove('disappear');
            }, 2000);
            ws.close();
            clearInterval(timeLimits_record);
        }



        function wsonclose(judge, ws) {
            if (judge) {
                animate_pkend();
            };
            console.log("已经与服务器断开连接当前连接状态：" + ws.readyState);
        };

        ws.onerror = function (event) {
            console.log("WebSocket异常！");
        };
    } catch (ex) {
        alert(ex.message);
    }
};

//重置pk匹配页面
function resetPK() {
    $('.enterPk .other .other_name').innerHTML = '??????';
    $('.pk_interface .other .other_name').innerHTML = '??????';
    $('.other .box .other_name').innerHTML = '??????';

    $('.newwaitPK .other .head_portrait').innerHTML = `<div class="Unmatched">?</div>`
    $('.pk_end .ohter_portrait').innerHTML = `<img src="./images/头像/头像-女学生2.png">`
    $('.enterPk .other .head_portrait').innerHTML = `<img src="./images/头像/头像-女学生2.png">`

    $('.enterPk .text_box').innerHTML = '';
    $('.enterPk .text_box').scrollTop = '0';

    $('.enterPk .mine .mine_blood_change').style.width = '38.13vw';
    $('.enterPk .name_blood .mine_blook').innerHTML = '100%';
    $('.enterPk .other .other_blood_change').style.width = '38.13vw';
    $('.enterPk .name_blood .other_blook').innerHTML = '100%';


    $('.pk_end').classList.remove('disappear');
    $('.pk_end .mine').classList.remove('disLeft');
    $('.pk_end .other').classList.remove('disRight');
    UserSelectArr = [];
    endAnswerArr = [];
}

//刷新id
function Refresh_id(data) {
    $('.enterPk .other .other_name').innerHTML = data;
    $('.pk_interface .other .other_name').innerHTML = data;
    $('.other .box .other_name').innerHTML = data;
}

//刷新头像
function Refresh_img(data) {
    if (data.base64 == '') {
        $('.newwaitPK .other .head_portrait').innerHTML = `<img src="./images/头像/头像-女学生2.png">`
        $('.pk_end .ohter_portrait').innerHTML = `<img src="./images/头像/头像-女学生2.png">`
        $('.enterPk .other .head_portrait').innerHTML = `<img src="./images/头像/头像-女学生2.png">`
    } else {
        $('.newwaitPK .other .head_portrait').innerHTML = `<img src="${data.base64}">`
        $('.pk_end .ohter_portrait').innerHTML = `<img src="${data.base64}">`
        $('.enterPk .other .head_portrait').innerHTML = `<img src="${data.base64}">`
    }

}

//刷新模板
function Refresh_Template(data) {
    $('.enterPk .text_box').innerHTML = data;
    // console.log(all('.enterPk .text_box div'))
    for (let x of all('.enterPk .text_box div')) {
        x.classList.add("highlight");
        x.classList.add("recite");
    }
    Refresh_answer();
}

/*窗口滚动缓动动画*/
function animate(obj, target, stop) {
    obj.scrollTop = stop;
    clearInterval(obj.timerx);
    obj.timerx = setInterval(function () {
        var step = (target - obj.scrollTop) / 15; //缓动动画位移
        if (obj.scrollTop.toFixed() == target.toFixed()) {
            clearInterval(obj.timerx); //如果运动到目标值时清除定时器
        };

        obj.scroll(0, obj.scrollTop + step);

    }, 10);
}

//刷新答案
function Refresh_answer() {
    //数组用来存放答案
    answerArr = [];
    selectArr = [];
    let slArr = [];
    let answerIndex = 0;

    let timerx = null;
    let timero = null;

    //当鼠标滚动时清除定时器
    $('.enterPk .text_box').addEventListener('touchstart', () => {
        clearInterval($('.enterPk .text_box').timerx);
        clearTimeout(timero);
    });

    // 随机排列数组
    function sortArr(arr) {
        arr.sort(function () {
            return 0.5 - Math.random();
        })
        return arr.slice(0, 4);
    }


    //将带特殊标签的隐藏答案
    Array.from(all('.enterPk .highlight')).forEach((x, i) => {
        x.classList.add('recite');
        answerArr.push(x.innerHTML);
        endAnswerArr.push(x.innerHTML);
        selectArr.push(x.innerHTML);
    });


    for (let x of $('.enterPk .option .com')) {
        x.onclick = () => {
            if (answerIndex < all('.enterPk .highlight').length) {
                if (x.querySelector('.answer').innerHTML == answerArr[answerIndex]) {
                    all('.enterPk .highlight')[answerIndex].classList.add('right');
                    UserSelectArr.push("right");
                    console.log(UserSelectArr)
                    ws.send(JSON.stringify({
                        answerName: x.querySelector('.answer').innerHTML,
                        answerValue: true,
                    }))

                } else {
                    all('.enterPk .highlight')[answerIndex].classList.add('wrong');
                    UserSelectArr.push("wrong");
                    console.log(UserSelectArr)
                    ws.send(JSON.stringify({
                        answerName: x.querySelector('.answer').innerHTML,
                        answerValue: false,
                    }))
                }
                // UserSelectArr.push(x.querySelector('.answer').innerHTML);
                if (answerIndex < all('.enterPk .highlight').length - 1) {
                    answerSelect();
                } else {
                    //再次刷新题目
                    ws.send("AGAIN");
                    console.log("发送再次刷新模板请求！");
                }

            }
        }

    }

    function strDisturbance() {
        let str = answerArr[Math.floor(Math.random() * answerArr.length)];
        while(str == answerArr[answerIndex]){
            str = answerArr[Math.floor(Math.random() * answerArr.length)];
        }
        
        let newStrAll = [];
        str.split('').forEach((item, index, array) => {
            let newIndex = Math.round(Math.random() * newStrAll.length);
            newStrAll.splice(newIndex, 0, item);
        });
        return newStrAll.join('');
    }

    //选择答案后
    function answerSelect() {
        answerIndex++;
        slArr = [];
        for (let x of selectArr) {
            if (x == answerArr[answerIndex])
                continue;
            slArr.push(x);
            if (slArr.length == 3)
                break;
        }
        if (answerArr.length < 4) {
            for (let i = 0; i < 4 - answerArr.length; i++) {
                let str = strDisturbance()
                let a = 0;
                for(let j = 0;j < slArr.length;j++){
                    if (slArr[j] == str)
                        a++;
                    if(j == slArr.length-1&&a != 0)
                        str = strDisturbance();
                }
                slArr.push(str);
            }
        }
        slArr.push(answerArr[answerIndex]);
        slArr = sortArr(slArr);
        console.log(selectArr, slArr);
        let n = 0;


        for (let x of $('.enterPk .option .answer')) {
            x.innerHTML = slArr[n++];
        }
        if (answerIndex < all('.enterPk .highlight').length) {
            let picsTop = all('.enterPk .highlight')[answerIndex].offsetTop - $('.enterPk .text_box').offsetTop;

            if (picsTop >= $('.enterPk .text_box').offsetTop / 2) {
                setTimeout(function () {
                    animate($('.enterPk .text_box'), picsTop - $('.enterPk .text_box').clientHeight / 2, $('.enterPk .text_box').scrollTop)
                }, 1000);

            }
        }
    }
    answerSelect();
    gameInit();

    function gameInit() {
        answerIndex = 0;
    }
    //pkend的界面渲染
}

//匹配成功之后的动画
function animate_success() {
    setTimeout(function () {
        $('.img_box img').style.opacity = '0';
        $('.enterPk').style.display = 'block';
        $('.enterPk').classList.add('appear');
        $('.img_box').classList.remove("xz");
        $('.img_box').classList.add("disappear_xz");
        $('.newwaitPK .mine').classList.add('disappearup')
        $('.newwaitPK .other').classList.add('disappearbottom')
        setTimeout(() => {
            $('.newwaitPK').style.display = 'none';
            $('.newwaitPK .mine').classList.remove('disappearup')
            $('.newwaitPK .other').classList.remove('disappearbottom')
            $('.img_box').classList.remove("disappear_xz");
        }, 2000);
    }, 2000);
}

//pk结束之后的动画
function animate_pkend() {
    $('.enterPk .head_nav_pk').classList.add('disappear');
    $('.enterPk .head_nav_pk').classList.add('animated');
    $('.enterPk .pk_blood .mine').classList.add('disLeft');
    $('.enterPk .pk_blood .mine').classList.add('animated');
    // $('.enterPk .pk_blood .enter_vs').classList.add('disappear');
    // $('.enterPk .pk_blood .enter_vs').classList.add('animated');
    $('.enterPk .pk_blood .time').classList.add('disappear');
    $('.enterPk .pk_blood .time').classList.add('animated');
    $('.enterPk .pk_blood .other').classList.add('disRight');
    $('.enterPk .pk_blood .other').classList.add('animated');
    $('.enterPk .text_box').classList.add('animated');
    $('.enterPk .text_box').classList.add('disappear');
    $('.enterPk .option').classList.add('animated');
    $('.enterPk .option').classList.add('disappear');
    $('.runle').style.display = 'none';
    setTimeout(function () {
        $('.pk_end').style.display = 'block';
        setTimeout(() => {
            $('.win_lose .win').style.display = "block";
            $('.win_lose .win').classList.add('zoom2');

        }, 1800)
        setTimeout(() => {
            $('.win_lose .bgc').style.display = "block";
            $('.win_lose .bgc').classList.add('zoom');
        }, 2400)
    }, 2000);
}

//刷线血条和血量
function Refresh_blook(data) {
    if (data[0].userId == curr.userId) {
        $('.enterPk .mine .mine_blood_change').style.width = (Math.round(data[0].hp) / 100) * 38.13 + 'vw';
        $('.enterPk .name_blood .mine_blook').innerHTML = Math.round(data[0].hp) + '%';
    } else {
        $('.enterPk .other .other_blood_change').style.width = (Math.round(data[0].hp) / 100) * 38.13 + 'vw';
        $('.enterPk .name_blood .other_blook').innerHTML = Math.round(data[0].hp) + '%';
    }

    if (data[1].userId == curr.userId) {
        $('.enterPk .mine .mine_blood_change').style.width = (Math.round(data[1].hp) / 100) * 38.13 + 'vw';
        $('.enterPk .name_blood .mine_blook').innerHTML = Math.round(data[1].hp) + '%';
    } else {
        $('.enterPk .other .other_blood_change').style.width = (Math.round(data[1].hp) / 100) * 38.13 + 'vw';
        $('.enterPk .name_blood .other_blook').innerHTML = Math.round(data[1].hp) + '%';
    }

}

//结束之后刷新星星和积分
function RefreshXing(winnerId) {
    if (winnerId == -1) {
        $('.pk_end .mine_add_reduce').innerHTML = `+0`;
        $('.pk_end .other_add_reduce').innerHTML = `+0`;
    } else {
        if (curr.userId == winnerId) {
            $('.pk_end .mine_add_reduce').innerHTML = `+1`;
            $('.pk_end .other_add_reduce').innerHTML = `-1`;
        } else {
            $('.pk_end .mine_add_reduce').innerHTML = `-1`;
            $('.pk_end .other_add_reduce').innerHTML = `+1`;

        }
    }
    //渲染排位
    rlRendering()
}

//刷新输赢动画
function RefreshWin_Lose(winnerId) {
    if (winnerId == -1) {
        $('.pk_end .win_lose .bgc').src = "./images/pk/win_bgc.png";
        $('.pk_end .win_lose .win').src = "./images/pk/win.png";
    } else {
        if (curr.userId == winnerId) {
            $('.pk_end .win_lose .bgc').src = "./images/pk/win_bgc.png";
            $('.pk_end .win_lose .win').src = "./images/pk/win.png";
        } else {
            $('.pk_end .win_lose .bgc').src = "./images/pk/defeat_bgc .png";
            $('.pk_end .win_lose .win').src = "./images/pk/defeat.png";
            $('.pk_end .win_lose .win').style.width = "29.33vw";
            $('.pk_end .win_lose .win').style.left = "2vh";
        }
    }
}

//结算界面点击退出
$(".pk_end .click_back ").addEventListener('click', () => {
    resetPK();
    $('.enterPk').classList.remove('appear');
    img_box.classList.remove("disappear_xz");
    $('.newwaitPK .mine').classList.remove('disappearup')
    $('.newwaitPK .other').classList.remove('disappearbottom')

    $('.enterPk .pk_blood .time').classList.remove('disappear');
    $('.enterPk .pk_blood .time').classList.remove('animated');
    $('.enterPk .head_nav_pk').classList.remove('disappear');
    $('.enterPk .pk_blood .mine').classList.remove('disLeft');
    $('.enterPk .pk_blood .other').classList.remove('disRight');
    $('.enterPk .text_box').classList.remove('disappear');
    $('.enterPk .option').classList.remove('disappear');

    $('.enterPk').style.display = 'none';
    $('.pk_end').classList.add('disappear');
    $('.pk_end .mine').classList.add('disLeft');
    $('.pk_end .other').classList.add('disRight');
    $('.pk_end .mine').classList.remove('appearLeft');
    $('.pk_end .other').classList.remove('appearRight');
    setTimeout(() => {
        $('.pk_end').style.display = 'none';
        $('.pk_end .win_lose .bgc').style.display = 'none';
        $('.pk_end .win_lose .win').style.display = 'none';

    }, 1300);
})