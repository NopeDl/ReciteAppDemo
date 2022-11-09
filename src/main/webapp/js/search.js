//搜索功能的实现
let header_search_memory = document.getElementById("header_search_memory");
let ul_context = $(".search_lis");

function createTemp(judge_personal) { //judge_personal：用于去判断是否是社区
    //获取记忆库的模板
    let tp_inner;
    let arr = [];
    $(".search_page .empty").style.display = 'none';
    if (judge_personal) {
        tp_inner = all(".me_base .tp_inner");
        console.log("记忆库搜索")
        for (let x of tp_inner) {
            let model_id = x.childNodes[1];
            let model_title = x.querySelector('.title');
            let model_label = x.querySelector('.label_title');
            let model_info = x.querySelector('.info');
            arr.push({ model_id: model_id.innerHTML, model_title: model_title.innerHTML, model_label: model_label.innerHTML, model_info: model_info.innerHTML })
        }
    } else {
        tp_inner = all(".community .community_ul li");
        console.log(tp_inner)
        console.log("社区搜索");
        for (let x of tp_inner) {
            if (!x.classList.contains('footer')) {
                let model_id = x.childNodes[0];
                let model_title = x.querySelector('.title');
                let model_label = x.querySelector('.label_title');
                let model_info = x.querySelector('.info_box .info');
                arr.push({ model_id: model_id.innerHTML, model_title: model_title.innerHTML, model_label: model_label.innerHTML, model_info: model_info.innerHTML })
            }
        }
    }
    //获取搜索框的值
    var search_value = header_search_memory.value;

    let arrnew = arr.map((item, index) => {
        //Object.assign方法用来将源对象（source）的所有可枚举属性，复制到目标对象（target）。
        console.log(item.model_title);
        return Object.assign({}, {
            'model_title': item.model_title,
            'model_id': item.model_id,
            'judgeUse': false,
        })

    });
    //filter()方法用于过滤数组元素。该方法创建一个新数组, 其中包含通过所提供函数实现的测试的所有元素。filter()不会对空数组进行检测，也不会改变原始数组。
    var newData = arrnew.filter(item => {
        if (item.model_title.indexOf(search_value) > -1) { //indexOf方法中如果xxx.indexOf("")返回值为0
            //console.log(item);
            item.judgeUse = true;
            return item;
        }
        return newData;
    });
    //如果没用完全匹配的话就模糊搜索
    for (let x of arrnew) {
        if (hasSameCharacter(x.model_title, search_value)) {
            if (!x.judgeUse) {
                newData.push(x);
            }

        }
    }

    if (newData.length == 0) {
        $(".search_page .empty").style.display = 'block';
        if (judge_personal) {
            $(".search_page .personal_empty").style.display = 'block';
            $(".search_page .community_empty").style.display = 'none';
        } else {
            $(".search_page .personal_empty").style.display = 'none';
            $(".search_page .community_empty").style.display = 'block';
        }
    }

    let content = "";
    if (newData.length > 0) {
        for (var i = 0; i < newData.length; i++) {
            for (let x of arr) {
                if (x.model_title === newData[i].model_title) {
                    for (let i = 0; i < search_value.length; i++) {
                        if (search_value[i] == ' ' || search_value[i] == `.`) {
                            continue;
                        }
                        //匹配字符
                        // g ：表示全局（global）模式，即模式将被应用于所有字符串，而非在发现第一个匹配项时立即停止；
                        //i： 表示不区分大小写（case -insensitive） 模式， 即在确定匹配项时忽略模式与字符串的大小写；
                        let pattern = new RegExp(`${search_value[i]}`, "gi");
                        let pattern_span1 = new RegExp(`<span class="searched">`, "gi");
                        let pattern_span2 = new RegExp(`</span>`, "gi");
                        let pattern_span3 = new RegExp(`m`, "gi");
                        let pattern_span4 = new RegExp(`k`, "gi");
                        let judge_change = false;

                        if (search_value[i] == 's' || search_value[i] == 'p' || search_value[i] == 'a' || search_value[i] == 'n' || search_value[i] == 'c' || search_value[i] == 'l' || search_value[i] == 'e' || search_value[i] == 'r' || search_value[i] == 'h' || search_value[i] == 'd' || search_value[i] == '>' || search_value[i] == '<' || search_value[i] == '/') {
                            if (x.model_title.indexOf(`<span class="searched">`)) {
                                console.log("sdgsdgs");
                            }
                            new_title = x.model_title.replace(pattern_span1, `m`);
                            new_title = new_title.replace(pattern_span2, `k`);
                            // new_title = x.model_title.replace(`</span>`, `   `);
                            judge_change = true;
                        }
                        if (judge_change) {
                            new_title = new_title.replace(pattern, match => `<span class="searched">${match}</span>`);
                            new_title = new_title.replace(pattern_span3, `<span class="searched">`);
                            new_title = new_title.replace(pattern_span4, `</span>`);
                            judge_change = false;
                        } else {
                            new_title = x.model_title.replace(pattern, match => `<span class="searched">${match}</span>`);
                        }
                        x.model_title = new_title;
                    }
                    // content += `
                    // <li class="baseLis_fadeIn">
                    //     <div class="tp_inner">
                    //         <div class="modleId">${x.model_id}</div>
                    //         <div class="content">
                    //             <h3 class="title ellipsis">${x.model_title}</h3>
                    //             <div class="info ellipsis">${x.model_info}</div>
                    //         </div>
                    //         <div class="tip">
                    //             <div class="date">2022-10-15</div>
                    //             <div class="label">
                    //                 <span class="iconfont icon-shuqianguanli"></span>
                    //                 <span>${x.model_label}</span>
                    //             </div>
                    //         </div>
                    //         <div class="common"></div>
                    //     </div>
                    // </li>`

                    content += `
                    <li class="baseLis_fadeIn">
                        <div class="tp_inner">
                            <div class="modleId">${x.model_id}</div>
                            <div class="content">
                                <h3 class="title ellipsis">${x.model_title}</h3>
                            </div>
                            <div class="bottom_box">
                                <div class="info ellipsis">${x.model_info}</div>
                                <div class="tip">
                                    <div class="date">2022-10-15</div>
                                    <div class="label">
                                        <span class="iconfont icon-shuqianguanli"></span>
                                        <span>${x.model_label}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="common"></div>
                        </div>
                    </li>
                    `
                }
            }
        }
    }
    ul_context.innerHTML = content;
    if (all(".search_page .title .searched").length > 1) {
        for (let i = 0; i < all(".search_page .title .searched").length; i++) {
            for (let x of all(".search_page .title .searched")) {
                console.log(x.nextSibling);
                if (x.nextSibling == null) {
                    continue;
                };
                if (x.nextSibling.className == 'searched') {
                    x.innerHTML += x.nextSibling.innerHTML;
                    x.nextSibling.innerHTML = '';
                }
            }
        }
        for (let i = 0; i < all(".search_page .title .searched").length; i++) {
            for (let x of all(".search_page .title .searched")) {
                if (x.nextSibling == null) {
                    continue;
                };
                if (x.nextSibling.innerHTML == '') {
                    $('.search_page .title').removeChild(all(".search_page .title .searched")[i + 1]);
                }
            }
        }
    }
}

let haha = '<km>haha';
console.log(haha);
haha = haha.replace(`<km>`, `<span class="searched">`)
console.log(haha);



//判断是否含有相同字符串
function hasSameCharacter(str1, str2) {
    let sum = 0;
    for (let i = 0; i < str1.length; i++) {
        if (str2.indexOf(str1[i]) > -1) {
            sum++;
        }
    }
    if (sum > 0) {
        return true;
    } else {
        return false;
    }
}

let judeg = true;
//记忆库点击进入搜索
$('.memory_base .header_search').onclick = () => {
    $('.search_page').style.display = 'block';
    judeg = true;
}

//社区点击进入搜索
$('.community .header_search').onclick = () => {
    $('.search_page').style.display = 'block';
    judeg = false;
}

header_search_memory.onchange = () => {
    if (header_search_memory.value != "") {
        createTemp(judeg);
    }
}

//退出搜索
$('.search_page .header_left').onclick = () => {
    header_search_memory.value = '';
    ul_context.innerHTML = '';
    $(".search_page .empty").style.display = 'none';
    $('.search_page').style.display = 'none';
    $('.memory_base .header_search input').value = '';
    $('.community  .header_search input').value = '';
}