Vue.component("HelloComponent", {
  template:
    '<div class="container">' +
        '<div class="row justify-content-center">' +
            '<div class="col-md-8">' +
                '<div class="card">' +
                    '<div class="card-header">Example Component</div>' +
                    '<div class="card-body">' +
                        "I'm an example component." +
                    "</div>" +
                    '<button class="btn btn-primary" @click="clickFunc">Кликни</button>' +
                "</div>" +
            "</div>" +
        "</div>" +
    "</div>",

  mounted: function () {
    alert("Hello");
  },

  methods: {
    clickFunc() {
      alert("Click");
    },
  },
});
