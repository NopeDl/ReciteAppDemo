let calendar = document.querySelector('.calendar')
let Today = null;
const month_names = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']

//获取当前日历的年月
function getYear_Mon() {
    let year = $(".year_picker .year").innerHTML; //获取当前的年
    let month = $(".month_picker").innerHTML; //获取当前的月份


}

//获取是否为闰年
isLeapYear = (year) => {
    return (year % 4 === 0 && year % 100 !== 0 && year % 400 !== 0) || (year % 100 === 0 && year % 400 === 0)
}

//获取各月的天数
getFebDays = (year) => {
    return isLeapYear(year) ? 29 : 28;
}

generateCalendar = (month, year) => {

    let calendar_days = calendar.querySelector('.calendar_days');
    let calendar_header_year = calendar.querySelector('#year');
    //每月的天数
    let days_of_month = [31, getFebDays(year), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

    calendar_days.innerHTML = ''

    //获取时间Date对象用于处理日期和时间
    let currDate = new Date();

    //返回表示年份的4位数
    if (!year) {
        year = currDate.getFullYear();
    }


    let curr_month = `${month_names[month]}`
    monthPicker.innerHTML = curr_month;
    calendar_header_year.innerHTML = year;

    let clock_day = [];

    //获取所有的日期
    ajax(`http://8.134.104.234:8080/ReciteMemory/user.do/getClockInRecord?userId=${curr.userId}&year=${year}&month=${month+1}`, 'get', '', (str) => {
        calendar_days.innerHTML = ''
        let newstr = JSON.parse(str).msg.data;
        console.log(newstr);
        let dateList = newstr.dateList;

        if (newstr.searchSuccess) {
            for (let i = 0; i < dateList.length; i++) {
                if (dateList[i].split('-')[2].charAt(0) == '0') {
                    clock_day[i] = dateList[i].split('-')[2].split('0')[1];
                } else {
                    clock_day[i] = dateList[i].split('-')[2];
                }
            }
        }



        //获取每个月的第一天
        let first_day = new Date(year, month, 1);

        for (let i = 0; i <= days_of_month[month] + first_day.getDay() - 1; i++) {
            let day = document.createElement('div');
            if (i >= first_day.getDay()) {
                day.classList.add('calendar_day_hover')
                for (let x of clock_day) {
                    if (x == i - 1) {
                        day.classList.add('clock_in');
                    }
                }

                day.innerHTML = i - first_day.getDay() + 1;
                // day.innerHTML += `<span></span>
                //                 <span></span>
                //                 <span></span>
                //                 <span></span>`
                if (i - first_day.getDay() + 1 === currDate.getDate() && year === currDate.getFullYear() && month === currDate.getMonth()) {
                    day.classList.add('curr_date');
                    if (day.classList.contains('clock_in')) {
                        $('.calendar_footer .situation')[0].innerHTML = '今日已打卡';
                        $('.calendar_footer .situation')[1].innerHTML = '已签到';
                    }
                    Today = day;
                }
            }
            calendar_days.appendChild(day);
        }

    }, true);
}

let month_list = calendar.querySelector('.month_list');

//月份显示
month_names.forEach((e, index) => {
    let month = document.createElement('div')
    month.innerHTML = `<div data-month="${index}">${e}</div>`
    month.querySelector('div').onclick = (e) => {
        month_list.classList.remove('show');
        curr_month.value = index;
        generateCalendar(index, curr_year.value);
        e.stopPropagation();
    }
    month_list.appendChild(month);
})


document.onclick = () => {
    month_list.classList.remove('show');
}

let monthPicker = calendar.querySelector('#month_picker')

monthPicker.onclick = (e) => {
    month_list.classList.add('show')
    e.stopPropagation();
}

let currDate = new Date();

let curr_month = { value: currDate.getMonth() }
let curr_year = { value: currDate.getFullYear() }



//生成对应年份和月份的日历表
generateCalendar(curr_month.value, curr_year.value);

//月份减1
document.querySelector('#prev_year').onclick = () => {
    --curr_year.value
    generateCalendar(curr_month.value, curr_year.value)
};
//月份加1
document.querySelector('#next_year').onclick = () => {
    ++curr_year.value
    generateCalendar(curr_month.value, curr_year.value)
}


//日历月份的点击事件
$('.calendar_footer .situation')[1].onclick = () => {
    let poststr = `userId=${curr.userId}&date=${dateFromat(currDate)}`
    if($('.calendar_footer .situation')[1].innerHTML == '签到'){
        ajax(`http://8.134.104.234:8080/ReciteMemory/user.do/clockIn`, 'post', poststr, (str) => {
            let newstr = JSON.parse(str).msg;
            console.log(newstr);
            if (newstr.data.isSuccess) {
                Today.classList.add('clock_in');
            }
        }, true);
    }
    $('.calendar_footer .situation')[0].innerHTML = '今日已打卡';
    $('.calendar_footer .situation')[1].innerHTML = '已签到';
}

// 1.定义格式化时间的方法
function dateFromat(dtstr) {
    const dt = new Date(dtstr);

    const y = dt.getFullYear();
    const m = padZero(dt.getMonth() + 1);
    const d = padZero(dt.getDate());
    return `${y}-${m}-${d}`
}

//定义补零的函数
function padZero(n) {
    return n > 9 ? n : '0' + n;
}
console.log(dateFromat(currDate));


//打卡页面点击返回
$('.calendar_page .left').onclick = () => {
    $('.calendar_page').style.left = '-100%';
    let curr_month = { value: currDate.getMonth() }
    let curr_year = { value: currDate.getFullYear() }
        //退出的时候重置操作
    generateCalendar(curr_month.value, curr_year.value);
}