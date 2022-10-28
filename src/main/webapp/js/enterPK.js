//数组用来存放答案
let answerArr = [];
let selectArr = [];
let UserSelectArr = [];
let slArr = [];
let answerIndex = 0;


function strSort(str) {
    let a = str;
    a = a.split('')
    sortArr(a);
    return a.toString().replace(/,/g, '');
}


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
    return arr;
}

Array.from(all('.enterPk .highlight')).forEach((x, i) => {
    x.classList.add('recite');
    answerArr.push(x.innerHTML);
    selectArr.push(x.innerHTML);
});


for (let x of $('.enterPk .option .com')) {
    x.onclick = () => {
        if (answerIndex < all('.enterPk .highlight').length) {
            if (x.querySelector('.answer').innerHTML == answerArr[answerIndex]) {
                all('.enterPk .highlight')[answerIndex].classList.add('right');
                UserSelectArr.push("right");
                console.log(UserSelectArr)

            } else {
                all('.enterPk .highlight')[answerIndex].classList.add('wrong');
                UserSelectArr.push("wrong");
                console.log(UserSelectArr)
            }
            // UserSelectArr.push(x.querySelector('.answer').innerHTML);
            if(answerIndex < all('.enterPk .highlight').length - 1)
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
        if(slArr.length == 3)
            break;
    }
    console.log(answerArr[answerIndex]);
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