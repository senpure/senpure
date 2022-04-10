
${bean.js.namespace}.${bean.js.name}.MessageDecoder = function () {
    this.getEmptyMessage = function () {
        return new ${bean.js.namespace}.${bean.js.name}();
    }
};
io.regMessageDecoder(${bean.js.namespace}.${bean.js.name}.MESSAGE_ID,new ${bean.js.namespace}.${bean.js.name}.MessageDecoder() ,"${bean.js.namespace}.${bean.js.name}");
