<template>
  <div id="app">
    <TodoHeader></TodoHeader>
    <!--
        v-on:addTodo : 하위컴포넌트에서 호출하는 이벤트.
        "addTodo" : 해당 컴포넌트에서 호출하는 method.
    -->
    <TodoInput v-on:addTodo="addTodo"></TodoInput>
    <!-- v-on과 @는 같은 의미 -->
    <TodoList v-bind:propsdata="todoItems" @removeTodo="removeTodo"></TodoList>
    <TodoFooter v-on:removeAll="clearAll"></TodoFooter>
  </div>
</template>

<script>
import TodoHeader from './components/TodoHeader.vue';
import TodoInput from './components/TodoInput.vue';
import TodoList from './components/TodoList.vue';
import TodoFooter from './components/TodoFooter.vue';

export default {
  data() {
    return {
      todoItems : []
    }
  },
  created() {
    if ( localStorage.length > 0 ) {
      //localStorage에 저장 된 아이템을 한번에 불러오는 API는 없음.
      //반복문으로 아이템을 불러와야 함.
      for ( var i=0; i<localStorage.length; i++ ) {

        this.todoItems.push( localStorage.key( i ) );
      }
    }
  },
  methods: {
    addTodo( todoItem ) {
        //로컬 스토리지에 ( 키, 값 ) 형태로 저장.
        //크롬 개발자도구 Application -> Local Storage -> http://localhost:8080 선택 시 확인 가능.
        ////활용도가 있어보임.
        localStorage.setItem( todoItem, todoItem );
        this.todoItems.push( todoItem );
    },
    clearAll() {

      if ( confirm( '모두 삭제 하시겠습니까?' ) ) {

        localStorage.clear();
        this.todoItems = [];
      }
    },
    removeTodo( todoItem, index ) {
      //localStorage 데이터 삭제.
      localStorage.removeItem( todoItem );

      //todoItems 배열에서 삭제.
      //데이터의 속성이 변하면서 화면에 즉시 반영하는 뷰의 반응성때문에 화면이 갱신 됨.
      this.todoItems.splice( index, 1 );
    }
  },
  components : {
    'TodoHeader' : TodoHeader,
    'TodoInput' : TodoInput,
    'TodoList' : TodoList,
    'TodoFooter' : TodoFooter
  }
}
</script>

<style>
body {
  text-align: center;
  background-color: #F6F6F8;
}
input {
  border-style: groove;
  width: 200px;
}
button {
  border-style: groove;
}
.shadow {
  box-shadow: 5px 10px 10px rgba(0, 0, 0, 0.03)
}
</style>
