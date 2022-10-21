let ws;
let answerArr = [];
let UserSelectArr = [];

function ConnectionClicked() {
    try {
        ws = new WebSocket(`ws://8.134.104.234:8080/ReciteMemory/PK/${curr.userId}/178/easy`) //连接服务器
        ws.onopen = function(event) {
            console.log("已经与服务器建立了连接当前连接状态：" + this.readyState);
        };

        let msgCount = 0; //接收信息的次数

        ws.onmessage = function(event) {
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
                    Refresh_id(res.enemyInf.nickName);
                    Refresh_img(res.enemyInf);
                    Refresh_Template(res.context);
                    ws.send("Ready");
                    console.log("已经向服务器发送Ready信号！");
                }
                //判断匹配是否成功，成功后渲染挖空内容和答题的答案,当isReady为true即可进入pk界面
                if (res.isReady) {
                    animate_success();
                    console.log("我已经准备好啦！");
                }
                if (res.hpInf) {
                    Refresh_blook(res.hpInf);
                }


                if (res.MATCH_END) {
                    for (let x of answerArr) {
                        $(".show_answer .answer").innerHTML += `
                            <div class="an">${x}</div>
                            `;
                    }

                    for (let x of UserSelectArr) {
                        if (x == "right") {
                            $(".show_answer .mine_an").innerHTML += `
                            <div class="dui">√</div>
                            `;
                        } else {
                            $(".show_answer .mine_an").innerHTML += `
                                <div class="cuo">×</div>`;
                        }
                    }
                    if (res.records) {
                        console.log(res.records);
                        console.log(res.records[0].userId);
                        if (res.records[0].userId != curr.userId) {
                            let other_answer = res.records[0].answersRecord;
                            console.log(other_answer);
                            for (let i = 0; i < other_answer.length; i++) {
                                if (other_answer[i].right) {
                                    $(".show_answer .other_an").innerHTML += `
                                    <div class="dui">√</div>
                                    `;
                                } else {
                                    $(".show_answer .other_an").innerHTML += `
                                        <div class="cuo">×</div>`;
                                }
                            }
                        }
                        console.log(res.records[1].userId);
                        if (res.records[1].userId != curr.userId) {
                            other_answer = res.records[1].answersRecord;
                            console.log(other_answer);
                            for (let i = 0; i < other_answer.length; i++) {
                                if (other_answer[i].right) {
                                    $(".show_answer .other_an").innerHTML += `
                                    <div class="dui">√</div>
                                    `;
                                } else {
                                    $(".show_answer .other_an").innerHTML += `
                                        <div class="cuo">×</div>`;
                                }
                            }
                        }
                    }

                }


            }

        };
        ws.onclose = function(event) {
            animate_pkend();
            console.log("已经与服务器断开连接当前连接状态：" + this.readyState);
        };
        ws.onerror = function(event) {
            console.log("WebSocket异常！");
        };
    } catch (ex) {
        alert(ex.message);
    }
};


function Refresh_id(data) {
    $('.enterPk .other .other_name').innerHTML = data;
    $('.pk_interface .other .other_name').innerHTML = data;
    $('.other .box .other_name').innerHTML = data;
}

function Refresh_img(data) {
    $('.newwaitPK .other .head_portrait').innerHTML = `<img src="${data.base64}">`
    $('.pk_end .ohter_portrait').innerHTML = `<img src="${data.base64}">`
    $('.enterPk .other .head_portrait').innerHTML = `<img src="${data.base64}">`
}

function Refresh_Template(data) {
    $('.enterPk .text_box').innerHTML = data;
    // console.log(all('.enterPk .text_box div'))
    for (let x of all('.enterPk .text_box div')) {
        x.classList.add("highlight");
        x.classList.add("recite");
    }
    Refresh_answer();
}



function Refresh_answer() {
    //数组用来存放答案
    answerArr = [];
    let selectArr = [];
    UserSelectArr = [];
    let slArr = [];
    let answerIndex = 0;

    let timerx = null;
    let timero = null;
    /*窗口滚动缓动动画*/
    function animate(obj, target, stop) {
        obj.scrollTop = stop;
        clearInterval(obj.timerx);
        obj.timerx = setInterval(function() {
            var step = (target - obj.scrollTop) / 15; //缓动动画位移
            if (obj.scrollTop.toFixed() == target.toFixed()) {
                clearInterval(obj.timerx); //如果运动到目标值时清除定时器
            };

            obj.scroll(0, obj.scrollTop + step);

        }, 10);
    }
    //当鼠标滚动时清除定时器
    $('.enterPk .text_box').addEventListener('touchstart', () => {
        clearInterval($('.enterPk .text_box').timerx);
        clearTimeout(timero);
    });

    // 随机排列数组
    function sortArr(arr) {
        arr.sort(function() {
            return 0.5 - Math.random();
        })
        return arr.slice(0, 4);
    }

    Array.from(all('.enterPk .highlight')).forEach((x, i) => {
        x.classList.add('recite');
        answerArr.push(x.innerHTML);
        selectArr.push(x.innerHTML);
        console.log(x.offsetHeight);
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
                if (answerIndex < all('.enterPk .highlight').length - 1)
                    answerSelect();
            } else {
                // for(let x of $('.enterPk .option .answer')){
                //     x.innerHTML = '';
                // }
            }
        }

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
                setTimeout(function() {
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
    $('.head_nav_pk .back').onclick = () => {
        $('.enterPk').style.display = 'none';
    }

    //pkend的界面渲染
}


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

//匹配成功之后的动画
function animate_success() {
    setTimeout(function() {
        $('.enterPk').style.display = 'block';
        $('.enterPk').classList.add('appear');
        img_box.classList.remove("xz");
        img_box.classList.add("disappear_xz");
        img_box.classList.add("animated");
        $('.newwaitPK .mine').classList.add('disappearup')
        $('.newwaitPK .other').classList.add('disappearbottom')
        $('.newwaitPK .mine').addEventListener('animationend', () => {
            $('.newwaitPK').style.display = 'none';
        })
    }, 3000);
}



//匹配结束之后的动画
function animate_pkend() {
    setTimeout(function() {
        $('.enterPk .head_nav_pk').classList.add('disappear');
        $('.enterPk .head_nav_pk').classList.add('animated');
        $('.enterPk .pk_blood .mine').classList.add('disLeft');
        $('.enterPk .pk_blood .mine').classList.add('animated');
        $('.enterPk .pk_blood .enter_vs').classList.add('disappear');
        $('.enterPk .pk_blood .enter_vs').classList.add('animated');
        // $('.enterPk .pk_blood .time').classList.add('disappear');
        // $('.enterPk .pk_blood .time').classList.add('animated');
        $('.enterPk .pk_blood .other').classList.add('disRight');
        $('.enterPk .pk_blood .other').classList.add('animated');
        $('.enterPk .text_box').classList.add('animated');
        $('.enterPk .text_box').classList.add('disappear');
        $('.enterPk .option').classList.add('animated');
        $('.enterPk .option').classList.add('disappear');
    }, 3000);

    // setTimeout(function() {
    //     $('.enterPk').style.display = 'none';
    // }, 4000);

    setTimeout(function() {
        $('.pk_end').style.display = 'block';
    }, 4000);
}

//渲染pk结束页面
// function pk_end(answer, mine_answer, other_answer) {

// }
$(".click_back").addEventListener('click', () => {
    $('.pk_end').style.display = 'none';
    $('.pk_end').style.display = 'none';
});