let api = [];
const apiDocListSize = 5
api.push({
    name: 'default',
    order: '1',
    list: []
})
api.push({
    name: '平台通用接口API',
    order: '2',
    list: []
})
api.push({
    name: 'EMIS平台接口API',
    order: '3',
    list: []
})
api[2].list.push({
    alias: 'EmisIdentityProviderController',
    order: '1',
    link: 'emisidentityprovider控制域:身份提供商读写控制类',
    desc: 'EmisIdentityProvider控制域:身份提供商读写控制类',
    list: []
})
api[2].list[0].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/list.do',
    desc: 'GetEmisIdentityProviderlist.获取身份提供商列表或分页,返回EmisIdentityProvider对象.',
});
api[2].list[0].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/{id}.do',
    desc: 'GetEmisIdentityProviderbyid.主键获取身份提供商详细信息.',
});
api[2].list[0].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/save.do',
    desc: 'EmisIdentityProviderOperation.新增身份提供商数据操作逻辑，根据操作类型，执行新增操作.',
});
api[2].list[0].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/update.do',
    desc: 'EmisIdentityProviderOperation.修改身份提供商数据操作逻辑，根据操作类型，执行修改操作.',
});
api[2].list[0].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/delete/{ids}.do',
    desc: 'EmisIdentityProviderOperation.删除身份提供商数据操作逻辑，根据操作类型，执行删除操作.',
});
api[2].list.push({
    alias: 'IdentityProviderController',
    order: '2',
    link: 'identityprovider控制域:身份提供商读写控制类',
    desc: 'IdentityProvider控制域:身份提供商读写控制类',
    list: []
})
api[2].list[1].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/list.do',
    desc: 'GetIdentityProviderlist.获取身份提供商列表或分页,返回IdentityProvider对象.',
});
api[2].list[1].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/{id}.do',
    desc: 'GetIdentityProviderbyid.主键获取身份提供商详细信息.',
});
api[2].list[1].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/save.do',
    desc: 'IdentityProviderOperation.新增身份提供商数据操作逻辑，根据操作类型，执行新增操作.',
});
api[2].list[1].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/update.do',
    desc: 'IdentityProviderOperation.修改身份提供商数据操作逻辑，根据操作类型，执行修改操作.',
});
api[2].list[1].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://linlan.net/emis_portal/api/provider/identity/delete/{ids}.do',
    desc: 'IdentityProviderOperation.删除身份提供商数据操作逻辑，根据操作类型，执行删除操作.',
});
api.push({
    name: '错误码列表',
    order: '4',
    list: []
})
api.push({
    name: '数据字典',
    order: '5',
    list: []
})
document.onkeydown = keyDownSearch;
function keyDownSearch(e) {
    const theEvent = e;
    const code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code == 13) {
        const search = document.getElementById('search');
        const searchValue = search.value;
        let searchGroup = [];
        for (let i = 0; i < api.length; i++) {

            let apiGroup = api[i];

            let searchArr = [];
            for (let i = 0; i < apiGroup.list.length; i++) {
                let apiData = apiGroup.list[i];
                const desc = apiData.desc;
                if (desc.indexOf(searchValue) > -1) {
                    searchArr.push({
                        order: apiData.order,
                        desc: apiData.desc,
                        link: apiData.link,
                        list: apiData.list
                    });
                } else {
                    let methodList = apiData.list || [];
                    let methodListTemp = [];
                    for (let j = 0; j < methodList.length; j++) {
                        const methodData = methodList[j];
                        const methodDesc = methodData.desc;
                        if (methodDesc.indexOf(searchValue) > -1) {
                            methodListTemp.push(methodData);
                            break;
                        }
                    }
                    if (methodListTemp.length > 0) {
                        const data = {
                            order: apiData.order,
                            desc: apiData.desc,
                            link: apiData.link,
                            list: methodListTemp
                        };
                        searchArr.push(data);
                    }
                }
            }
            if (apiGroup.name.indexOf(searchValue) > -1) {
                searchGroup.push({
                    name: apiGroup.name,
                    order: apiGroup.order,
                    list: searchArr
                });
                continue;
            }
            if (searchArr.length === 0) {
                continue;
            }
            searchGroup.push({
                name: apiGroup.name,
                order: apiGroup.order,
                list: searchArr
            });
        }
        let html;
        if (searchValue == '') {
            const liClass = "";
            const display = "display: none";
            html = buildAccordion(api,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        } else {
            const liClass = "open";
            const display = "display: block";
            html = buildAccordion(searchGroup,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        }
        const Accordion = function (el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;
            const links = this.el.find('.dd');
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
        };
        Accordion.prototype.dropdown = function (e) {
            const $el = e.data.el;
            $this = $(this), $next = $this.next();
            $next.slideToggle();
            $this.parent().toggleClass('open');
            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp("20").parent().removeClass('open');
            }
        };
        new Accordion($('#accordion'), false);
    }
}

function buildAccordion(apiGroups, liClass, display) {
    let html = "";
    let doc;
    if (apiGroups.length > 0) {
         if (apiDocListSize == 1) {
            let apiData = apiGroups[0].list;
            for (let j = 0; j < apiData.length; j++) {
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#_' + apiData[j].link + '">' + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                html += '<ul class="sectlevel2" style="'+display+'">';
                doc = apiData[j].list;
                for (let m = 0; m < doc.length; m++) {
                    let spanString;
                    if (doc[m].deprecated == 'true') {
                        spanString='<span class="line-through">';
                    } else {
                        spanString='<span>';
                    }
                    html += '<li><a href="#_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].href + '">' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
                }
                html += '</ul>';
                html += '</li>';
            }
        } else {
            for (let i = 0; i < apiGroups.length; i++) {
                let apiGroup = apiGroups[i];
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#_' + apiGroup.name + '">' + apiGroup.order + '.&nbsp;' + apiGroup.name + '</a>';
                html += '<ul class="sectlevel1">';

                let apiData = apiGroup.list;
                for (let j = 0; j < apiData.length; j++) {
                    html += '<li class="'+liClass+'">';
                    html += '<a class="dd" href="#_'+apiGroup.order+'_'+ apiData[j].order + '_'+ apiData[j].link + '">' +apiGroup.order+'.'+ apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                    html += '<ul class="sectlevel2" style="'+display+'">';
                    doc = apiData[j].list;
                    for (let m = 0; m < doc.length; m++) {
                       let spanString;
                       if (doc[m].deprecated == 'true') {
                           spanString='<span class="line-through">';
                       } else {
                           spanString='<span>';
                       }
                       html += '<li><a href="#_'+apiGroup.order+'_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].desc + '">'+apiGroup.order+'.' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
                   }
                    html += '</ul>';
                    html += '</li>';
                }

                html += '</ul>';
                html += '</li>';
            }
        }
    }
    return html;
}