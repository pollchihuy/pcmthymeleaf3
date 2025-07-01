// let zIndexCounter = 1050;
// function getNextZIndex() {
//   return ++zIndexCounter;
// }

// (function($) {
//   $.fn.draggable = function(contents) {
//     contents = $.extend({
//       cursor : "move",
//       onDragStart : function() { },
//       onDragging : function() { },
//       onDragEnd : function() { }
//     }, contents);

//     var onDragStart = contents.onDragStart;
//     var onDragging = contents.onDragging;
//     var onDragEnd = contents.onDragEnd;
//     var $element = this;

//     return $element.css('cursor', contents.cursor).on("mousedown", function(e) {
//       onDragStart($(this));
//       var $element = $(this).addClass('draggable');

//       // 🔥 Tambahkan ini biar modal yang di-drag selalu paling atas
//       // $element.css('z-index', getNextZIndex());

//       var drg_w = $element.outerWidth(),
//           drg_h = $element.outerHeight(),
//           pos_y = $element.offset().top + drg_h - e.pageY,
//           pos_x = $element.offset().left + drg_w - e.pageX;

//       $element.parents().on("mousemove", function(e) {
//         onDragging(e);
//         $('.draggable').offset({
//           top :  e.pageY + pos_y - drg_h,
//           left : e.pageX + pos_x - drg_w
//         }).on("mouseup", function() {
//           $element.parents().unbind('mousemove');
//           $(this).removeClass('draggable');
//           // 🔥 Jangan reset z-index ke awal! Biar tetap di atas
//           // .css('z-index', z_idx);
//         });
//       });

//       e.preventDefault();
//     }).on("mouseup", function() {
//       $(this).removeClass('draggable');
//       onDragEnd($(this));
//     });
//   }
// })(jQuery);



// let zIndexCounter = 1050;
// function getNextZIndex() {
//   return ++zIndexCounter;
// }

(function($) {
  $.fn.draggable = function(contents) {
    contents = $.extend({
      cursor: "move",
      handle: null, // ✅ Tambahan handle
      onDragStart: function () {},
      onDragging: function () {},
      onDragEnd: function () {}
    }, contents);

    var onDragStart = contents.onDragStart;
    var onDragging = contents.onDragging;
    var onDragEnd = contents.onDragEnd;
    var $element = this;

    return $element.css('cursor', contents.cursor).on("mousedown", function(e) {
      // ✅ Hanya allow drag kalau klik dari handle (jika handle ditentukan)
      if (contents.handle && !$(e.target).closest(contents.handle).length) {
        return; // Batal drag
      }

      onDragStart($(this));

      var $element = $(this).addClass('draggable');

      // $element.css('z-index', getNextZIndex());


      var drg_w = $element.outerWidth(),
          drg_h = $element.outerHeight(),
          pos_y = $element.offset().top + drg_h - e.pageY,
          pos_x = $element.offset().left + drg_w - e.pageX;

      $element.parents().on("mousemove", function(e) {
        onDragging(e);
        $('.draggable').offset({
          top: e.pageY + pos_y - drg_h,
          left: e.pageX + pos_x - drg_w
        }).on("mouseup", function() {
          $element.parents().unbind('mousemove');
          $(this).removeClass('draggable');
          // .css('z-index', z_idx); // juga tetap dikomen
        });
      });

      e.preventDefault();
    }).on("mouseup", function() {
      $(this).removeClass('draggable');
      onDragEnd($(this));


    });
  }
})(jQuery);

$(".modal-dialog").draggable({
  handle: ".modal-header", // cuma bisa drag dari header
  cursor: "move"
});