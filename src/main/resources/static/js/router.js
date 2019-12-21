function Router() {
    this.routes = {};//存储hash和方法的映射
    this.currentUrl = '';
}
var thisUrl='';

//注册路径和方法的映射
Router.prototype.route = function(path, callback) {

    this.routes[path] = callback;
};
//hash改变时触发的事件
Router.prototype.refresh = function() {
    this.currentUrl = location.hash.slice(1) || '/';
    thisUrl=this.currentUrl;
    this.routes[this.currentUrl]();
};

Router.prototype.init = function() {
    window.addEventListener('load', this.refresh.bind(this), false);
    window.addEventListener('hashchange', this.refresh.bind(this), false);
};

window.Router = new Router();
window.Router.init();