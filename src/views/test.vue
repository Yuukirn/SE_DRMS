<template>
	<div>
		<input type="text" v-model="keyWord" style="border: 1rpx solid gray;" placeholder="请输入查找">
		<table>
			<tbody>
				<tr v-for="(item,index) in filterList" :key="index">
					<td>{{item.name}}</td>
					<td>{{item.age}}</td>
				</tr>
			</tbody>
		</table>
	</div>
</template>
 
<script>
	export default {
		data() {
			return {
				keyWord: '',
				 list:[
				        { name: '张三', age: '18' },
				        { name: '张四', age: '17' },
				        { name: '张五', age: '17' },
				        { name: '老六', age: '18' },
				        { name: '老八', age: '18' },
				        { name: '小三', age: '19' },
				        { name: 'Xingyue', age: '18' }
				          ]
			}
		},
		computed:{
			filterList(){
				return this.list.filter(item=>{
					return item.name.indexOf(this.keyWord)!==-1 || item.age.indexOf(this.keyWord)!==-1
				})
			}
		}
	}
</script>
 
<style>
</style>