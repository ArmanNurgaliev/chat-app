let stompClient = null;
let chatDate = null;
let monthNames = ["January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

let recipientName = null;
let currentUserName = null;

function getChats() {
    $.ajax({
        type: "GET",
        url: "/get-chats",
        success: function (response) {
            $(".contacts").html('');
            response.forEach(function (room) {
                let numOfNewMessages = 0;
                room.chatMessages.forEach(function (message) {
                    if (message.senderName !== currentUserName && message.status === 'DELIVERED') {
                        numOfNewMessages++;
                    }
                });
                showChatRooms(room, numOfNewMessages);
            });
        }
    });
}

function showChatRooms(room, numOfNewMessages) {
    let tableData = '';
    let lastMessage = room.chatMessages.slice(-1)[0].content;
    if (numOfNewMessages !== 0) {
        tableData = '<li class="active user-list">' +
            '<div class="d-flex bd-highlight">' +
            '<div class="img_cont">' +
            '<img src="https://static.turbosquid.com/Preview/001292/481/WV/_D.jpg" class="rounded-circle user_img">' +
            '<span class="online_icon"></span>' +
            '</div>' +
            '<div class="user_info">' +
            '<span class="username-value">' + room.senderName + '</span>' +
            '<span class="notification" id="notification">' + numOfNewMessages + '</span>' +
            '<span class="last-message">' + lastMessage + '</span>' +
            '</div>' +
            '</div>' +
            '</li>';
    }
    else {
        tableData = '<li class="active user-list">' +
            '<div class="d-flex bd-highlight">' +
            '<div class="img_cont">' +
            '<img src="https://static.turbosquid.com/Preview/001292/481/WV/_D.jpg" class="rounded-circle user_img">' +
            '<span class="online_icon"></span>' +
            '</div>' +
            '<div class="user_info">' +
            '<span class="username-value">' + room.senderName + '</span>' +
            '<span class="last-message">' + lastMessage + '</span>' +
            '</div>' +
            '</div>' +
            '</li>';
    }
    $(".contacts").append(tableData);
}

function connect() {
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/queue/messages/' + currentUserName, function (message) {
            showMessage(JSON.parse(message.body));
            getChats();
        });
    });
}

function readMessages() {
    $.ajax({
        type: "GET",
        url: "/read-messages",
        dataType: 'json',
        data: {"recipientName": recipientName},
        contentType: 'application/json; charset=utf-8',
        success: function () {
        }
    });
    getChats();
}

function sendPrivateMessage() {
    let content = $("#content").val();
    if (content.trim() !== "") {
        recipientName = document.getElementById("chat-with").innerHTML;
        $.ajax({
            type: "GET",
            url: "/get-current-username",
            success: function (response) {
                let message = {
                    'content': content,
                    'senderName': response,
                    'timestamp': new Date(),
                    'recipientName': recipientName
                };
                stompClient.send("/app/private-messages/" + recipientName, {}, JSON.stringify(message));
                showMessage(message);
            }
        });
        document.getElementById('content').value = '';
    }
}


function showMessage(message) {
    let time = new Date(message.timestamp);
    $.ajax({
        type: "GET",
        url: "/get-current-username",
        success: function (response) {

            if (message.senderName === response) {
                showSendMessages(message, time);
            } else if (message.senderName === recipientName){
                showReceivedMessages(message, time);
            }
            let chatHistory = document.getElementById("messages");
            chatHistory.scrollTop = chatHistory.scrollHeight;
        }
    });
}

function getUserByName() {
    $.ajax({
        type: "GET",
        url: "/get-recipient",
        dataType: 'json',
        data: {"recipientName": recipientName},
        contentType: 'application/json; charset=utf-8',
        success: function (response) {
            return JSON.stringify(response);
        }
    });
}

function getCurrentUserName() {
    $.ajax({
        type: "GET",
        url: "/get-current-username",
        success: function (response) {
            currentUserName = response;
        }
    });
    connect();
}

function showMessagesFromDB(recipientName) {
    $("#chat-window").show();
    $("#chat-with").html(recipientName);
    $.ajax({
        url: '/get-chat',
        type: 'GET',
        dataType: 'json',
        data: {"recipientName": recipientName},
        contentType: 'application/json; charset=utf-8',
        success: function (messages) {
            $("#num-of-messages").html(messages.length + ' messages');
            $("#messages").html('');
            messages.forEach(function (message) {
                let time = new Date(message.timestamp);

                if (message.recipientName === recipientName) {
                    showSendMessages(message, time);
                } else {
                    showReceivedMessages(message, time);
                }
            });
            let chatHistory = document.getElementById("messages");
            chatHistory.scrollTop = chatHistory.scrollHeight;
        }
    });
    readMessages();
}

function showTime(time) {
    let showDate = "";
    if (chatDate == null || time.getDate() !== chatDate.getDate() || time.getFullYear() !== chatDate.getFullYear()) {
        showDate += (time.getDate() + " " + monthNames[time.getMonth()]);
        if (chatDate != null && time.getFullYear() !== chatDate.getFullYear())
            showDate += (" " + time.getFullYear());
        chatDate = time;
        let timeBox = '<div class="d-flex justify-content-center-date mb-4">' +
            '<div class="date-line">' +
                showDate +
            '</div>' +
            '</div>';

        $("#messages").append(timeBox);
    }
}

function showSendMessages(message, time) {
    showTime(time);
    let minutes = (time.getMinutes() < 10 ? '0' : '') + time.getMinutes();
    let msg = '<div class="d-flex justify-content-end mb-4">' +
        '<div class="msg_container_send">' +
        message.content +
        '<div>' +
        '<span class="msg_time_send">' +
        time.getHours() + ':' + minutes +
        '</span>' +
        '</div>' +
        '</div>' +
        '</div>';
    $("#messages").append(msg);
}

function showReceivedMessages(message, time) {
    showTime(time);
    let minutes = (time.getMinutes() < 10 ? '0' : '') + time.getMinutes();
    let msg = '<div class="d-flex justify-content-start mb-4">' +
        '<div class="msg_container">' +
        message.content +
        '<div>' +
        '<span class="msg_time">' +
        time.getHours() + ':' + minutes +
        '</span>' +
        '</div>' +
        '</div>' +
        '</div>';
    $("#messages").append(msg);
}

function searchFriends() {
    var arrayReturn = []
    $.ajax({
        url: "/get-users",
        dataType: 'json',
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                var id = (data[i].id).toString();
                arrayReturn.push({
                    'value': data[i].username,
                    'data': id
                })
            }
            printSupplier(arrayReturn);
        }
    });

    function printSupplier(options) {
        $('#tags').autocomplete({
            source: options,
            select: function(req, result) {
                showMessagesFromDB(result.item.value);
            }
        });
    }
}

$(document).ready(function(){
    $("#chat-window").hide();
    getCurrentUserName();
    getChats();

    $("#tags").on('keyup', function() {
        searchFriends();
    });

    $('#action_menu_btn').click(function(){
        $('.action_menu').toggle();
    });

    $("#send").click(function(){
        sendPrivateMessage();
        getChats();
        return false;
    });

    $(document).on('click','.active',function () {
        recipientName = $(this).find(".username-value").text();
        chatDate = null;
        showMessagesFromDB(recipientName);
    });
});