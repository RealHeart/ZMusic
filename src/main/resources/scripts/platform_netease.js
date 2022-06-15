// noinspection JSUnresolvedFunction,JSUnresolvedVariable,ES6ConvertVarToLetConst

/**
 * JS扩展平台使用示例
 * 插件提供以下函数:
 * 1.http.get(url: string): string
 * 2.http.post(url: string, data: any): string
 *
 * 注意: 不支持ES6, 仅支持ES5.1以下语法
 */

var name = "netease"
var name_zh_CN = "网易云音乐"

var api = "https://netease.api.zhenxin.xyz"

function searchPage(keyword, page, count, offset) {
    var search = http_get(api +
        '/cloudsearch?keywords=' +
        encodeURI(keyword) +
        '&limit=' + count +
        '&offset=' + offset)
    var data = JSON.parse(search)
    var result = data.result
    var songs = result.songs
    songs.forEach((it) => {
        var id = it.id
        musics.add(getMusicInfo(id))
    })
    return musics
}


function getMusicInfo(id) {
    var data = http.get(api + '/song/detail?ids=' + id)
    var song = data.songs[0]
    var name = song.name
    var singers = song.ar
    var singer = mergeSingers(singers)
    var album = song.al
    var albumName = album.name
    var albumImage = album.picUrl
    var duration = song.dt

    return {
        id,
        name,
        singer,
        albumName,
        albumImage,
        duration
    }
}