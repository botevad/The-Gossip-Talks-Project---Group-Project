const baseURL = window.location;
//const baseURL = 'http://localhost:8000';

$('#changePasswordButton').click(changePassword);
$('#postGossip').submit(postGossip);
$("#menu-toggle").click(function (e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});

function getCurrentUser() {
    var url = new URL(baseURL);
    url.pathname = '/api/v1/users/me';
    fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            $('#currentUser').text(data.username);
        });
}

function getUsers() {
    var url = new URL(baseURL);
    url.pathname = '/api/v1/users';
    fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            var usersFriends = $('#usersFriends').empty();
            var usersOthers = $('#usersOthers').empty();
            data.forEach(user => {
                var where = user.following ? usersFriends : usersOthers
                where.append(
                    `<a
            href="javascript:userFollow('${user.username}', ${!user.following})"
            class="list-group-item list-group-item-action bg-light">
            <i class="fa fa-${user.following ? 'minus' : 'plus'}-circle"></i>
            ${user.username}
          </a>`);
            });
        });
    return false;
}

function changePassword() {
    var url = new URL(baseURL);
    url.pathname = '/api/v1/users/me';
    fetch(url, {
        method: 'post',
        body: new FormData(document.getElementById('passwordForm')),
    }).then(function (response) {
        if (response.status != 200) {
            $('#changePasswordModal .alert').show();
        } else {
            $('#changePasswordModal').modal('hide');
        }
        return response.text();
    });
}

function userFollow(username, follow) {
    var url = new URL(baseURL);
    var form = new FormData();
    form.append('follow', follow);
    url.pathname = `/api/v1/users/${username}/follow`;
    fetch(url, {
        method: 'post',
        body: form,
    }).then(function (response) {
        return response.text();
    });
    getUsers();
    return false;
}

function getGossips() {
    var url = new URL(baseURL);
    url.pathname = '/api/v1/gossips';
    fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            console.log(data);
            var posts = $('#posts').empty();
            data.content.forEach(gossip => {
                posts.append(`
          <a href="#" class="list-group-item list-group-item-action">
            <div class="d-flex w-100 justify-content-between">
              <h5>${gossip.username} <small class="text-muted">#${gossip.id}</small></h5>
              <small>${moment(gossip.datetime).fromNow()}</small>
            </div>
            <p class="mb-1">${gossip.text}</p>
          </a>
        `);
            });
        });
}

function postGossip() {
    var url = new URL(baseURL);
    url.pathname = '/api/v1/gossips';
    fetch(url, {
        method: 'post',
        body: new FormData(this),
    }).then(function (response) {
        return response.text();
    });
    return false;
}

getUsers();
getCurrentUser();
getGossips();
