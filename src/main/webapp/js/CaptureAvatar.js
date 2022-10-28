//截取头像
var can_obj = document.querySelector("#canvas"); //获取画板
var img_obj = document.querySelector("#canvas_img"); //获取需要截取的图片对象
//记录缩放的值
let scaleValue = 1;

loadCapture();

function loadCapture() {
    //设置图片自适应大小及图片的居中显示
    autoResizeImage(img_obj);
    //为画布盒子增添监听事件
    document.querySelector("#canvas_div").addEventListener('touchstart', touch);
    document.querySelector("#canvas_div").addEventListener('touchmove', touch);
    //使用getContext()方法返回一个用于在画布上绘图的环境。
    var capture_img = can_obj.getContext("2d");
    // 'x':divObj.offsetLeft,'y':divObj.offsetTop
    capture_X = can_obj.offsetLeft;
    capture_Y = can_obj.offsetTop;
    //图片坐标
    let img_left = img_obj.offsetLeft;
    let img_top = img_obj.offsetTop;
    console.log("can_obj.offsetWidth" + can_obj.offsetWidth)
    console.log("can_obj.Width" + can_obj.width)

    capture_img.drawImage(img_obj, capture_X, capture_Y, img_obj.width, img_obj.height); //初始化 canvas 加入图片

    //触摸移动事件
    function touch(e) {
        let event = e || window.event;
        event.preventDefault(); //阻止浏览器或body或者其他冒泡事件
        //手指坐标
        let fingerPos_x1 = event.touches[0].clientX;
        let fingerPos_y1 = event.touches[0].clientY;

        //图片坐标
        img_left = img_obj.offsetLeft;
        img_top = img_obj.offsetTop;

        //单只手操作滑动
        if (event.touches.length == 1) {
            //开始移动
            if (event.type == "touchstart") {
                //获取img相对坐标
                posX = fingerPos_x1 - img_obj.offsetLeft;
                posY = fingerPos_y1 - img_obj.offsetTop;

                //获取手指开始移动的位置
                var startY = event.touches[0].pageY;
                var startX = event.touches[0].pageX;
            }
            //移动ing
            else if (event.type == "touchmove") {
                //获取手指结束的位置
                let endY = event.changedTouches[0].clientY;
                let endX = event.touches[0].clientX;
                //移动坐标
                var movX = fingerPos_x1 - posX;
                var movY = fingerPos_y1 - posY;
                //记录手指移动的方向及距离
                let disY = startY - endY;
                let disX = startX - endX;

                //右边界
                let boundaryRight;

                //获取画布距离右侧的距离
                canX = can_obj.offsetLeft;

                //获取图片距离右侧的距离
                imgX = img_obj.offsetLeft;

                //获取画布距离顶部的距离
                canY = can_obj.offsetTop;

                //获取图片距离右侧的距离
                imgY = img_obj.offsetTop;

                if (disX > disY) {
                    console.log("disX > disY");
                    // img_obj.style.left = movX + "px";
                    console.log(movY + "movY");

                    img_obj.style.top = movY + "px";

                    //清除画布
                    capture_img.clearRect(0, 0, can_obj.offsetWidth, can_obj.offsetHeight);
                    console.log("can_obj.offsetWidth" + can_obj.offsetWidth);
                    //画布内图片移动
                    // capture_img.drawImage(img_obj, img_top, img_left + movX, img_obj.width * scaleValue, img_obj.height * scaleValue);
                    // capture_img.drawImage(img_obj, capture_X, movY - parseFloat(can_obj.style.top) + left_y / 2, img_obj.width * scaleValue, img_obj.height * scaleValue);

                } else {
                    console.log("disX < disY");
                    // boundaryRight = img_obj.offsetWidth - can_obj.offsetWidth;
                    console.log(boundaryRight + "boundaryRight");
                    console.log(img_obj.offsetLeft + "img_obj.style.left");
                    if (canX - imgX < 0) {

                        // img_obj.style.left = "50%";

                        // img_obj.style.marginLeft = -(img_obj.style.left / 2) + "px";
                    } else if ((img_obj.offsetWidth + img_obj.offsetLeft) < (can_obj.offsetWidth + can_obj.offsetLeft)) {
                        let dis_X = img_obj.offsetWidth - can_obj.offsetWidth - (can_obj.offsetLeft);
                        console.log("dis_X" + dis_X)
                        console.log("can_obj.offsetLeft " + can_obj.offsetLeft)
                        img_obj.style.left = -dis_X + "px";
                        img_obj.style.marginLeft = -(dis_X / 2) + "px";
                    } else {
                        img_obj.style.left = movX + "px";
                        img_obj.style.marginLeft = -(movX / 2) + "px";
                    }
                    console.log("img_obj.offsetLeft:" + img_obj.offsetLeft)

                    // console.log(movX + "movX");
                    // img_obj.style.top = movY + "px";
                    // capture_img.drawImage(img_obj, img_top, img_left + movX, img_obj.width * scaleValue, img_obj.height * scaleValue);
                    //清除画布
                    capture_img.clearRect(0, 0, can_obj.offsetWidth, can_obj.offsetHeight);
                    // console.log("can_obj.offsetWidth" + can_obj.offsetWidth);
                    // capture_img.drawImage(img_obj, img_top + movY, img_left, img_obj.offsetWidth * scaleValue, img_obj.offsetHeight * scaleValue);

                }

            }
        }
        //双指操作
        else if (event.touches.length == 2) {
            if (event.type == "touchstart") {

            } else if (event.type == "touchmove") {

            }
        }
    }

}



//让图片与画布自适应
function autoResizeImage(img_obj) {
    var img_w = img_obj.offsetWidth;
    console.log(img_w);
    var img_h = img_obj.offsetHeight;
    console.log(img_h);
    //获取图片宽高的比率
    let ratio = img_w / img_h;
    console.log(ratio);

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