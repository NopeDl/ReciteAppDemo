//数组用来存放答案
let answerArr = [];
let selectArr = [];
let UserSelectArr = [];
let slArr = [];
let answerIndex = 0;
window.addEventListener('click', function() {

})
let timerx = null;
let timero = null;
/*窗口滚动缓动动画*/
function animate(obj, target, stop) {
    obj.scrollTop = stop;
    console.log(stop);
    clearInterval(obj.timerx);
    obj.timerx = setInterval(function() {
        var step = (target - obj.scrollTop) / 15; //缓动动画位移
        if (obj.scrollTop.toFixed() == target.toFixed()) {
            clearInterval(obj.timerx); //如果运动到目标值时清除定时器
        };
        
        // obj.scroll(0, obj.scrollTop + step);
        obj.scroll(0, obj.scrollTop + step);

    }, 10);
}
//当鼠标滚动时清除定时器
$('.enterPk .text_box').addEventListener('touchstart', () => {
    clearInterval($('.enterPk .text_box').timerx);
    clearTimeout(timero);
});

// setInterval(() => {
//     console.log($('.enterPk .text_box').scrollTop);
// }, 500);


// 随机排列数组
function sortArr(arr) {
    arr.sort(function() {
        return 0.5 - Math.random();
    })
    return arr.slice(0, 4);
}

Array.from(all('.highlight')).forEach((x, i) => {
    x.classList.add('recite');
    answerArr.push(x.innerHTML);
    selectArr.push(x.innerHTML);
    console.log(x.offsetHeight);
});

// .clientHeight/2
// console.log($('.enterPk .text_box').clientHeight / 2);
// $('.enterPk .text_box').onscroll = () => {
//     for (let i = 0; i < all('.highlight').length; i++) {
//         let picsTop = all('.highlight')[i].offsetTop - $('.enterPk .text_box').offsetTop;
//         console.log($('.enterPk .text_box').scrollTop);
//     }
// }

for (let x of $('.enterPk .option .com')) {
    x.onclick = () => {
        if (answerIndex < all('.highlight').length) {
            // $('.enterPk .text_box').scrollTop = `${answerIndex * 40}px`
            if (x.querySelector('.answer').innerHTML == answerArr[answerIndex]) {
                all('.highlight')[answerIndex].classList.add('right');
                UserSelectArr.push("right");
                console.log(UserSelectArr)

            } else {
                all('.highlight')[answerIndex].classList.add('wrong');
                UserSelectArr.push("wrong");
                console.log(UserSelectArr)
            }
            // UserSelectArr.push(x.querySelector('.answer').innerHTML);
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
    slArr = sortArr(selectArr);
    console.log(selectArr, answerArr);
    let n = 0;

    for (let x of $('.enterPk .option .answer')) {
        x.innerHTML = slArr[n++];
    }
    if (answerIndex < all('.highlight').length) {
        let picsTop = all('.highlight')[answerIndex].offsetTop - $('.enterPk .text_box').offsetTop;

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
    $('.enterPk').style.left = '-100%';
}

//pkend的界面渲染