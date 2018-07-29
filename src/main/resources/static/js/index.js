$(function () {

    $('#table').DataTable({
        processing: true, // 必须加上这个才能显示加载中的效果
        serverSide: false,
        stateSave: true,
        paging: true, // 是否开启本地分页
        pagingType: "full_numbers",
        lengthMenu: [[5, 10, 25, 50, -1], /*后面的数组是显示翻译前面的数字*/[5, 10, 25, 50, "All"]], // 定义每页显示数据数量
        displayLength: 5,
        ordering: true,
        info: true,
        autoWidth: true,
        language: {
            // 这里很重要，如果你的"加载中"是文字，则直接写上文字即可，如果是gif的图片，使用img标签就可以加载
            // "processing: "<img src='/images/loading.gif'>",
            processing: '<i class="fa fa-spinner fa-pulse fa-3x fa-fw"></i>',
            lengthMenu: "显示 _MENU_ 项结果",
            zeroRecords: "没有匹配结果",
            info: "显示第 _START_ 至 _END_ 项结果,共 _TOTAL_ 项",
            infoEmpty: "显示第 0 至 0 项结果，共 0 项",
            infoFiltered: "(由 _MAX_ 项结果过滤)",
            search: "搜索:",
            emptyTable: "表中数据为空",
            loadingRecords: "载入中...",
            paginate: {
                first: "首页",
                previous: "上一页",
                next: "下一页",
                last: "末页"
            },
            aria: {
                sortAscending: ":以升序排列此列",
                sortDescending: ":以降序排列此列"
            },
            buttons: {
                pageLength: { // 选择每页条数按钮
                    '-1': '显示全部',
                    _: '显示 %d 项结果'
                }
            },
            select: {
                rows: {
                    _: '%d 行选中',
                    0: '单击行选中',
                    1: '1 行选中'
                }
            }
        },
        // 文件导出
        dom: 'Bfrtip',
        buttons: [
            {
                extend: 'pageLength' // 选择每页条数按钮
            },
            {
                extend: 'copyHtml5',
                text: '<i class="fa fa-files-o"></i>',
                titleAttr: '复制表格内容',
                exportOptions: {
                    columns: ':visible' // 仅操作可见列
                }
            },
            {
                extend: 'excelHtml5',
                text: '<i class="fa fa-file-excel-o"></i>',
                titleAttr: '导出Excel',
                title: 'dataTablesExport', // 导出的文件名
                exportOptions: {
                    columns: ':visible' // 仅操作可见列
                }
            },
            {
                extend: 'csvHtml5',
                text: '<i class="fa fa-file-text-o"></i>',
                titleAttr: '导出CSV',
                title: 'dataTablesExport',
                exportOptions: {
                    columns: ':visible' // 仅操作可见列
                }
            },
            {
                extend: 'pdfHtml5',
                text: '<i class="fa fa-file-pdf-o"></i>',
                titleAttr: '导出PDF',
                title: 'dataTablesExport',
                exportOptions: {
                    columns: ':visible' // 仅操作可见列
                }
            },
            {
                extend: 'print',
                autoPrint: true, // 自动弹出打印预览
                text: '<i class="fa fa-print"></i>',
                titleAttr: '打印',
                exportOptions: {
                    columns: ':visible', // 仅操作可见列
                }
            },
            {
                extend: 'colvis', // 选择显示列按钮
                columnText: function (dt, idx, title) {
                    return (idx + 1) + ': ' + title;
                },
                postfixButtons: [{
                    extend: 'colvisRestore',
                    text: '重置'
                }],
                text: '<i class="fa fa-list"></i>',
                titleAttr: '隐藏/显示列'
            }
        ],
        select: true,
        ajax: {
            url: "/data.json",
            // "contentType: "application/json",
            type: "GET",
            dataSrc: "data",
            data: function (param) {
                param.timestamp = new Date().getTime();
                // return JSON.stringify(param); // post请求
            }
        },
        //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
        //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
        columns: [
            {
                data: 'name',
                title: '姓名',
                defaultContent: ''
            },
            {
                data: 'position',
                title: '职位',
                defaultContent: ''
            },
            {
                data: 'salary',
                title: '工资',
                render: function (data, type, full, meta) {
                    return data || '';
                }
            },
            {
                data: 'office',
                title: '办公地点',
                defaultContent: ''
            }
        ]
    });

});