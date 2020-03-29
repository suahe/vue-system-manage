function getDictByValue(row,column,dicData){
    debugger
    let results = row[column.property]; // 默认原真实值
    if(dicData && dicData.length>0){
        for(let i=0;i<dicData.length;i++){
            let item = dicData[i];
            if(row[column.property] === item.value){
                results = item.name;
                break;
            }
        }
    }
    return results;
};
export default {
    getDictByValue
}