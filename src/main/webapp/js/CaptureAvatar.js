//截取头像
var can_obj = document.querySelector("#canvas"); //获取画板
var img_obj = document.querySelector("#canvas_img"); //获取需要截取的图片对象
//记录缩放的值
let scaleValue = 1;



// var haha = document.querySelector("#haha");
// var original = document.querySelector("#CaptureBox");
// var npt = document.getElementById("npt");
// npt.onchange = function() {
//     var reader = new FileReader();
//     reader.readAsDataURL(npt.files[0]);
//     reader.onloadend = function(e) {
//         img_obj.src = e.target.result;
//         // console.log(e.target.result);// 图片的base64数据
//         original.style.display = "block"
//         haha.style.display = "none"
//         setTimeout(() => {
//             loadCapture();
//         },50);
        
//     };
// };



// loadCapture();

function loadCapture() {
    //设置图片自适应大小及图片的居中显示
    autoResizeImage(img_obj);

    //使用getContext()方法返回一个用于在画布上绘图的环境。
    var capture_img = can_obj.getContext("2d");
    // 'x':divObj.offsetLeft,'y':divObj.offsetTop
    capture_X = can_obj.offsetLeft;
    capture_Y = can_obj.offsetTop;
    capture_W = can_obj.offsetWidth;
    capture_H = can_obj.offsetHeight;

    //记录在缩放动作执行前的 缩放值
    var scale = 0;
    //开始缩放比例
    var start_sqrt = 0;
    var sqrt = 1;
    
    can_obj.width = Math.round(63.3*(document.body.clientWidth/100));
    can_obj.height = Math.round(63.3*(document.body.clientWidth/100));
    console.log(img_obj.clientWidth,can_obj.width);
    let ctx_X = (img_obj.clientWidth - can_obj.width) / 2;
    let ctx_Y = (img_obj.clientHeight - can_obj.height) / 2;
    capture_img.drawImage(img_obj, -ctx_X, -ctx_Y, img_obj.width, img_obj.height); //初始化 canvas 加入图片


    $("#canvas_div").ontouchstart = (e) => {
        img_obj.classList.remove('canvas_img_ani');
        let event = e || window.event;
        event.preventDefault();
        //单指操作
        if (event.touches.length == 1) {
            //记录手指的位置
            let touch_x = e.changedTouches[0].clientX;
            let touch_y = e.changedTouches[0].clientY;
            let Pos_y1 = e.changedTouches[0].clientY - img_obj.offsetTop;
            let Pos_x1 = e.changedTouches[0].clientX - img_obj.offsetLeft;

            $("#canvas_div").ontouchmove = (e) => {
                let endY = e.changedTouches[0].clientY;
                let endX = e.changedTouches[0].clientX;
                let movX = endX - Pos_x1;
                let movY = endY - Pos_y1;
                img_obj.style.top = (movY / (document.body.clientWidth/100)) + "vw";
                img_obj.style.left = (movX / (document.body.clientWidth/100)) + "vw";
                capture_img.clearRect(0, 0, can_obj.width, can_obj.height); //清除画布
                capture_img.drawImage(img_obj, -ctx_X + img_obj.offsetLeft - capture_X, -ctx_Y + img_obj.offsetTop - capture_Y, img_obj.width * sqrt, img_obj.height * sqrt); //画布内图片移动

                $("#canvas_div").ontouchend = (e) => {

                    img_obj.classList.add('canvas_img_ani');
                    //最左边
                    if (movX - img_obj.offsetWidth / 2 > capture_X - capture_W / 2) {
                        img_obj.style.left = ((capture_X + img_obj.offsetWidth / 2 - capture_W / 2) / (document.body.clientWidth/100)) + "vw";
                    }
                    //最右边
                    if (movX + img_obj.offsetWidth / 2 < capture_X + capture_W / 2) {
                        img_obj.style.left = ((capture_X + capture_W / 2 - img_obj.offsetWidth / 2) / (document.body.clientWidth/100)) + "vw";
                    }

                    //最上边
                    if (movY - img_obj.offsetHeight / 2 > capture_Y - capture_H / 2) {
                        img_obj.style.top = ((capture_Y + img_obj.offsetHeight / 2 - capture_H / 2) / (document.body.clientWidth/100)) + "vw";
                    }
                    //最下边
                    if (movY + img_obj.offsetHeight / 2 < capture_Y + capture_H / 2) {
                        img_obj.style.top = ((capture_Y + capture_H / 2 - img_obj.offsetHeight / 2) / (document.body.clientWidth/100)) + "vw";
                    }

                    setTimeout(function() {
                        clearInterval(timers);
                    }, 300)
                    let timers = setInterval(() => {
                        capture_img.drawImage(img_obj, -ctx_X + img_obj.offsetLeft - capture_X, -ctx_Y + img_obj.offsetTop - capture_Y, img_obj.width * sqrt, img_obj.height * sqrt); //画布内图片移动
                    }, 0.1)

                }
            }
        }
        //双指操作
        else if (event.touches.length == 2) {
            scale = img_obj.style.Transform == undefined ? 1 : parseFloat(img_obj.style.Transform.replace(/[^0-9^\.]/g, "")); //获取在手指按下瞬间的放大缩小值（scale），作用，在移动时，记录上次移动的放大缩小值

            //获取在缩放时 当前缩放的值
            start_X1 = event.touches[0].clientX;
            start_Y1 = event.touches[0].clientY;
            start_X2 = event.touches[1].clientX;
            start_Y2 = event.touches[1].clientY;
            start_sqrt = Math.sqrt((start_X2 - start_X1) * (start_X2 - start_X1) + (start_Y2 - start_Y1) * (start_Y2 - start_Y1)) / can_obj.width;
            console.log(start_sqrt);

            $("#canvas_div").ontouchmove = (e) => {
                let mv_y1 = e.changedTouches[0].clientY;
                let mv_x1 = e.changedTouches[0].clientX;
                var mv_x2 = event.touches[1].clientX,
                    mv_y2 = event.touches[1].clientY;
                var move_sqrt = Math.sqrt((mv_x2 - mv_x1) * (mv_x2 - mv_x1) + (mv_y2 - mv_y1) * (mv_y2 - mv_y1)) / can_obj.width; //动态获取上一次缩放值(随时变更)，在下次缩放时减去上一次的值，作用：防止累加之前的缩放

                let movX = endX - Pos_x1;
                let movY = endY - Pos_y1;
                img_obj.style.top = (movY / (document.body.clientWidth/100)) + "vw";
                img_obj.style.left = (movX / (document.body.clientWidth/100)) + "vw";
                sqrt = move_sqrt - start_sqrt + scale; //求出缩放值
                //设置放大缩小
                img_obj.style.webkitTransform = "scale(" + sqrt + ")";
                img_obj.style.Transform = "scale(" + sqrt + ")";

                capture_img.clearRect(0, 0, can_obj.width, can_obj.height); //清除画布
                capture_img.drawImage(img_obj, -ctx_X + img_obj.offsetLeft - capture_X, ctx_Y + img_obj.offsetTop - capture_Y, img_obj.width * sqrt, img_obj.height * sqrt); //画布内图片移动

                $("#canvas_div").ontouchend = (e) => {
                    img_obj.classList.add('canvas_img_ani');
                    //最左边
                    if (movX - img_obj.offsetWidth / 2 > capture_X - capture_W / 2) {
                        img_obj.style.left = ((capture_X + img_obj.offsetWidth / 2 - capture_W / 2) / (document.body.clientWidth/100)) + "vw";
                    }
                    //最右边
                    if (movX + img_obj.offsetWidth / 2 < capture_X + capture_W / 2) {
                        img_obj.style.left = ((capture_X + capture_W / 2 - img_obj.offsetWidth / 2) / (document.body.clientWidth/100)) + "vw";
                    }

                    //最上边
                    if (movY - img_obj.offsetHeight / 2 > capture_Y - capture_H / 2) {
                        img_obj.style.top = ((capture_Y + img_obj.offsetHeight / 2 - capture_H / 2) / (document.body.clientWidth/100)) + "vw";
                    }
                    //最下边
                    if (movY + img_obj.offsetHeight / 2 < capture_Y + capture_H / 2) {
                        img_obj.style.top = ((capture_Y + capture_H / 2 - img_obj.offsetHeight / 2) / (document.body.clientWidth/100)) + "vw";
                    }

                    setTimeout(function() {
                        clearInterval(timers);
                    }, 300)
                    let timers = setInterval(() => {
                        capture_img.drawImage(img_obj, -ctx_X + img_obj.offsetLeft - capture_X, ctx_Y + img_obj.offsetTop - capture_Y, img_obj.width * sqrt, img_obj.height * sqrt); //画布内图片移动
                    }, 0.1)
                }
            }
        }


    }

}

function save_img() {
    try {
        console.log('截图开始');
        console.log(can_obj);
        var base64 = can_obj.toDataURL('image/jpeg', 1 || 0.8);
        console.log('base64:');
        console.log(base64);
        var original = document.querySelector("#CaptureBox");
        var later_box = document.querySelector("#later_box");
        var later = document.querySelector("#later");
        original.style.display = 'none';
        later_box.style.display = 'block';
        console.log(later.src)
        later.src = `${base64}`;
    } catch (e) {
        console.log(e);
    }
}

//让图片与画布自适应
function autoResizeImage(img_obj) {
    var img_w = img_obj.offsetWidth;
    // console.log(img_w);
    var img_h = img_obj.offsetHeight;
    // console.log(img_h);
    //获取图片宽高的比率
    let ratio = img_w / img_h;
    // console.log(ratio);

    if (ratio > 1) {
        //img_obj.height = 63.3;
        img_obj.style.height = '63.3vw'
        img_obj.style.width = 'auto'
    } else {
        //img_obj.width = 63.3;
        img_obj.style.width = '63.3vw'
        img_obj.style.height = 'auto'
    }
}

//点击退出头像裁剪
$('.canvas_cancel').onclick = () => {
    $('.CaptureAvatar_page').style.display = 'none';
}