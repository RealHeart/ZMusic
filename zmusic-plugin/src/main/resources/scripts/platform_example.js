// noinspection JSUnresolvedFunction,JSUnresolvedVariable,ES6ConvertVarToLetConst

/**
 * JS扩展平台使用示例
 *
 * 文件名: platform_平台名称.js
 * 示例: platform_netease.js
 *
 * 插件提供以下函数:
 * http.get(url: string): string
 * http.post(url: string, data: any): string
 * util.mergeSingers(singers: any): string
 *
 * 需暴露以下函数(具体返回查看下方示例):
 * platform(lang: string): string
 * search(keyword: string): any
 * searchPage(keyword: string, page: int, count: int): any
 * playlist(id: string): any
 * album(id: string): any
 * url(id: string): string
 * lyric(id: string): any
 * info(id: string): any
 *
 * 注意: 不支持ES6, 仅支持ES5.1以下语法
 */

/**
 * 获取平台名称
 * @param lang 语言
 */
function platform(lang) {
    switch (lang) {
        case 'zh_CN':
            return '示例平台'
        case 'ja_JP':
            return 'プラットフォーム例'
        default:
            return 'Example Platform'
    }
}


/**
 * 单首搜索音乐
 * @param keyword 关键词
 */
function search(keyword) {
    return {
        id: '',
        name: '',
        singer: '',
        albumName: '',
        albumImage: '',
        duration: ''
    }
}

/**
 * 分页搜索音乐
 * @param keyword 关键词
 * @param page 页数
 * @param count 每页数量
 */
function searchPage(keyword, page, count) {
    return [
        {
            id: '',
            name: '',
            singer: '',
            albumName: '',
            albumImage: '',
            duration: 0
        }
    ]
}

/**
 * 获取歌单信息
 * @param id 歌单ID
 */
function playlist(id) {
    return {
        id: '',
        name: '',
        musics: [
            {
                id: '',
                name: '',
                singer: '',
                albumName: '',
                albumImage: '',
                duration: 0
            }
        ]
    }
}

/**
 * 获取播放链接
 * @param id 音乐ID
 */
function url(id) {
    return ''
}

/**
 * 获取专辑信息
 * @param id 专辑ID
 */
function album(id) {
    return {
        id: '',
        name: '',
        image: '',
        musics: [
            {
                id: '',
                name: '',
                singer: '',
                duration: 0
            }
        ]
    }
}

/**
 * 获取歌词信息
 * @param id 音乐ID
 */
function lyric(id) {
    return [
        {
            time: 0,
            content: '',
            translation: ''
        }
    ]
}

/**
 * 获取音乐信息
 * @param id 音乐ID
 */
function info(id) {
    return {
        id: '',
        name: '',
        singer: '',
        albumName: '',
        albumImage: '',
        duration: ''
    }
}