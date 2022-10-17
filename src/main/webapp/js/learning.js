//数组用来存放被选中的节点
let arr1 = [];
//数组用来存放答案
let arr2 = [];

let btn = $('.learn_page .header_right li');
let learn_flag_1 = true;
let learn_flag_2 = true;
//点击进入答题模式
btn[1].onclick = () => {
    if (learn_flag_1 && flag_learn && document.querySelector('.learn_page .highlight')) {
        arr2 = [];
        learnReset();
        //利用循环将选中的节点内容替换
        for (let x of all('.learn_page .highlight')) {
            arr2.push(x.innerText);
            x.setAttribute('contenteditable', true)
            x.setAttribute('tabindex', '-1')
            x.innerHTML = '请输入答案';
            // 点击可输入答案
            x.onclick = (e) => {
                answer(x);
                e.stopPropagation();
            };
            x.classList.add('input');
        }

        function answer(x) {
            for (let k of all('.learn_page .highlight')) {
                if (k.innerHTML == '')
                    k.innerHTML = '请输入答案';
            }
            if (x.innerHTML == '请输入答案')
                x.innerHTML = ''
        }
        learn_flag_1 = false;
        learn_flag_2 = true;
    } else {
        // 再次点击退出答题模式
        learnReset()
        let n = 0;
        for (let x of all('.learn_page .highlight')) {
            if (!learn_flag_1)
                x.innerHTML = arr2[n++];
        }
        learn_flag_1 = true;
    }
}

//点击页面其他地方时，如果填入内容为空则将内容修改
document.onclick = () => {
        if (flag_learn && document.querySelector('.learn_page .highlight')) {
            for (let x of all('.learn_page .input')) {
                if (x.innerHTML == '')
                    x.innerHTML = '请输入答案';
            }
        }
    }
    //点击进入背诵模式
btn[2].onclick = () => {
    if (learn_flag_2 && flag_learn && document.querySelector('.learn_page .highlight')) {
        learnReset()
            //利用循环将选中的节点添加类
        let n = 0;
        for (let x of all('.learn_page .highlight')) {
            if (!learn_flag_1)
                x.innerHTML = arr2[n++];
            x.classList.add('recite');
            x.onclick = () => {
                x.classList.toggle('recite');
            }
        }
        learn_flag_2 = false;
        learn_flag_1 = true;
    } else {
        learnReset();
        learn_flag_2 = true;
    }
}

//点击提交答案
btn[0].onclick = () => {
    if (!learn_flag_1) {
        learn_flag_1 = true;
        let n = 0,
            sum = 0;
        for (let x of all('.learn_page .highlight')) {
            if (x.innerHTML == arr2[n])
                sum++;
            n++;
        }
        let a = 0;
        for (let x of all('.learn_page .highlight'))
            x.innerHTML = arr2[a++];
        alert('正确率：' + (sum / n) * 100 + '%');
        learnReset();
    }

}


//将节点重置回原本状态
function learnReset() {
    if (document.querySelector('.learn_page .highlight')) {
        for (let x of all('.learn_page .highlight')) {
            x.setAttribute('contenteditable', false)
            x.className = 'highlight';
            x.onclick = null;
        }
    }
}