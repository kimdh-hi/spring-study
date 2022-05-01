function download(id) {
    console.log(id)
    $.get("/download/" + id, null, function(data) {
        console.log(data)
    })
}