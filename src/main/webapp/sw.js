// importScripts("https://storage.googleapis.com/workbox-cdn/releases/3.1.0/workbox-sw.js");
// var cacheStorageKey = 'minimal-pwa-1'
// var cacheList = ['/','index.html','logo.png']

const CACHE_NAME = 'cache_2'
self.addEventListener('install', async (e) => {
    console.log('install');
    //开启cache，得到cache对象
    const cache = await caches.open(CACHE_NAME);

    await cache.addAll([
        './index.html',
        './images/logo.png',
        './json/manifest.json',
        './js/base.js',
        './js/index.js',
        './js/indexAjax.js',
        './js/edit.js',
        './js/learning.js',
        './js/calendar.js',
        './js/pk.js',
        './js/MakingTemplates.js',
        './js/personal.js',
        './js/community.js',
        './js/smPK.js',
        './iconfont/iconfont.css',
        './css/index.css',
        './css/calendar.css',
        './css/pk.css',
        './css/MakingTemplates.css',
        './css/personal.css',
        './css/community.css',
        './css/newEnterPK.css',
        './css/ViewTemplate.css',
        './login.html',
        './css/login.css',
        './js/login.js',
        './iconfont/iconfont.json',
        './iconfont/iconfont.ttf',
        './iconfont/iconfont.woff',
        './iconfont/iconfont.woff2',
        './iconfont/iconfont.js',
    ])

    await self.skipWaiting();
})

self.addEventListener('activate', async (e) => {
    console.log('activate');
    const keys = await caches.keys();

    keys.forEach(key => {
        if (key !== CACHE_NAME)
            caches.delete(key);
    })

    await self.clients.claim();
})

// self.addEventListener('fetch', function (e) {
//     console.log('fetch');
//     //给浏览器响应
//     e.respondWith(networkFirst(e.request));
// })


//网络优先
async function networkFirst(req) {
    try {
        //先从网络中读取
        const fresh = await fetch(req)
        return fresh;
    } catch (e) {
        const cache = await caches.open(CACHE_NAME);
        const cached = await cache.match(req);
        return cached;
    }
}

//缓存优先
async function cacheFirst() {
    try {
        //先从缓存中读取
        const cache = await caches.open(CACHE_NAME);
        const cached = await cache.match(req);
        return cached;
    } catch (e) {
        const fresh = await fetch(req)
        return fresh;
    }
}

//缓存资源
// self.addEventListener('install', e => {
//     console.log('install',e);
//     //等待skipwaiting结束才进入activate
//     e.waitUntil(
//         caches.open(cacheStorageKey)
//             .then(cache => cache.addAll(cacheList))
//             //让service worker跳过等待，直接进入activate
//             .then(() => self.skipWaiting())
//     )
// })

//删除旧的资源
// self.addEventListener('activate', function (e) {
//     console.log('activate',e);
//     e.waitUntil(
//         //获取所有cache名称
//         caches.keys().then(cacheNames => {
//             return Promise.all(
//                 // 获取所有不同于当前版本名称cache下的内容
//                 cacheNames.filter(cacheNames => {
//                     return cacheNames !== cacheStorageKey
//                 }).map(cacheNames => {
//                     return caches.delete(cacheNames)
//                 })
//             )
//         }).then(() => {
//             //service worker激活后，立即获取控制权
//             return self.clients.claim()
//         })
//     )
// })

//操作缓存和读取网络资源，发送请求的时候触发
// self.addEventListener('fetch', function (e) {
//     console.log('fetch', e);
//     e.respondWith(
//         caches.match(e.request).then(function (response) {
//             if (response != null) {
//                 return response
//             }
//             return fetch(e.request.url)
//         })
//     )
// })