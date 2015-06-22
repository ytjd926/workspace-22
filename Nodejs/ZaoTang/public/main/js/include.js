var urls = document.getElementsByTagName("script");
var urls = urls[urls.length-1].src;
var url = urls.slice(urls.indexOf("?")+1);
var ieid = url.replace(/\W/m,"_");



document.write('<IE:Download ID="'+ieid+'" STYLE="behavior:url(#default#download)" />');
document.getElementById(ieid).startDownload(url,onDownloadDone);
function onDownloadDone(downDate){
    alert(downDate);

    document.write(downDate);
}