var popups = [];

$(function() {
  enhance();
});

function enhance() {
  logins    = $('.login');
  registers = $('.register');
  login_legend = $('legend.login');
  
  div      = $('#login-div');
  form     = $('#login-form');
  email    = $('input[name=email]');
  password = $('input[name=password]');
  password_confirmation = $('input[name=password_confirmation]');
  submit   = $('#login');
  newacc   = $('input[name=newaccount]');
  
  _method        = $('input[name=_method]');
  route_dest     = $('input[name=route_dest]')[0].value;
  route_login    = $('input[name=route_login]')[0].value;
  route_register = $('input[name=route_register]')[0].value;
  
  // Remove hack to centre div using table style
  div.unwrap();
  div.unwrap();
  
  registers.hide();
  
  $(window).resize(function(ev) {
    div.offset({left: ($(window).width() - div.width()) / 2, top: ($(window).height() - div.height()) / 2});
    
    popups.forEach(function(popup) {
      popup.move();
    });
  });
  
  disableRegister();
  submit.hide();
  
  $(document).keypress(function(ev) {
    if(ev.which === 13) {
      form.submit();
    }
  });
  
  form.submit(function(ev) {
    if(newacc.is(':checked')) {
      register();
    } else {
      login();
    }
    
    ev.preventDefault();
  });
  
  newacc.click(function(ev) {
    if(newacc.is(':checked')) {
      enableRegister();
    } else {
      disableRegister();
    }
    
    $('input[autofocus]').focus();
  });
  
  $('input[autofocus]').focus();
  $(window).resize();
}

function register() {
  disableForm();
  console.log(route_register);
  
  $.ajax({
    url:  route_register,
    type: 'PUT',
    data: form.serialize()
  }).done(function(data, textStatus, jqXHR) {
    window.location.replace(route_dest);
  }).fail(function(jqXHR, textStatus, errorThrown) {
    switch(jqXHR.status) {
      case 401:
        
        break;
      
      case 409:
        var errors = jqXHR.responseJSON;
        for(var error in errors) {
          createPopup(errors[error], $('input[name=' + error + ']'));
        }
        
        break;
      
      default:
        console.log('This shouldn\'t happen:');
        console.log(textStatus);
        console.log(errorThrown);
        console.log(jqXHR.responseJSON);
    }
    
    enableForm();
  });
}

function login() {
  disableForm();
  
  $.ajax({
    url:  route_login,
    type: form[0].method,
    data: form.serialize()
  }).done(function(data, textStatus, jqXHR) {
    window.location.replace(route_dest);
  }).fail(function(jqXHR, textStatus, errorThrown) {
    switch(jqXHR.status) {
      case 401:
        
        break;
      
      case 409:
        var errors = jqXHR.responseJSON;
        for(var error in errors) {
          createPopup(errors[error], $('input[name=' + error + ']'));
        }
        
        break;
      
      default:
        console.log('This shouldn\'t happen:');
        console.log(textStatus);
        console.log(errorThrown);
        console.log(jqXHR.responseJSON);
    }
    
    enableForm();
  });
}

function disableForm() {
  email.prop('readonly', true);
  password.prop('readonly', true);
  password_confirmation.prop('readonly', true);
}

function enableForm() {
  email.prop('readonly', false);
  password.prop('readonly', false);
  password_confirmation.prop('readonly', false);
}

function disableRegister() {
  _method.prop('disabled', true);
  //password_confirmation.hide();
  //password_confirmation.prop('disabled', true);
  registers.hide();
  registers.prop('disabled', true);
  login_legend.show();
  
  $(window).resize();
}

function enableRegister() {
  _method.prop('disabled', false);
  //password_confirmation.show();
  //password_confirmation.prop('disabled', false);
  registers.show();
  registers.prop('disabled', false);
  login_legend.hide();
  
  $(window).resize();
}

function createPopup(text, anchor) {
  var popup = {
    element: $('<div class="popup">' + text + '</div>'),
    anchor: anchor,
    move: function() {
      var left = this.anchor.offset().left + this.anchor.outerWidth();
      var top  = this.anchor.offset().top + (this.anchor.outerHeight() - this.element.outerHeight()) / 2;
      this.element.offset({left: left, top: top});
    }
  };
  
  $('body').append(popup.element);
  
  popup.move();
  popups.push(popup);
}