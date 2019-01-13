<template>
    <div class="inputBox shadow">
      <input type="text" id="todoitem" v-model="newTodoItem" placeholder="To Do List Data"
             v-on:keyup.enter="addTodo">
      <span class="addContainer" v-on:click="addTodo">
        <i class="addBtn fas fa-plus" aria-hidden="true"></i>
      </span>
    </div>
</template>

<script>
export default {
  data() {
    return {
      newTodoItem : 'Hello'
    }
  },
  methods: {
    addTodo() {

      if ( this.newTodoItem !== "" ) {

        //여기서의 this는 해당 component
        var value = this.newTodoItem.trim();

        //상위 컴포넌트 이벤트 호출 및 인자 값 전달.
        //인자 값이 상위컴포넌트에서 변경되어도 하위 컴포넌트에는 영향이 없음.
        this.$emit( 'addTodo', value );
        this.clearInput();

      } else {

         alert( 'input value is empty!' );
      }

      document.getElementById( 'todoitem' ).focus();

    },
    clearInput() {

      this.newTodoItem = '';
    }
  }
}
</script>

<style scoped>
  input:focus {
    outline: none;
  }
  .inputBox {
    background: white;
    height: 50px;
    line-height: 50px;
    border-radius: 5px;
  }
  .inputBox input {
    border-style: none;
    font-size: 0.9rem;
  }
  .addContainer {
    float: right;
    background: linear-gradient(to right, #6478FB, #8763FB);
    display: block;
    width: 3rem;
    border-radius: 0 5px 5px 0;
    cursor: pointer;
  }
  .addBtn {
    color: white;
    vertical-align: middle;
  }
</style>
