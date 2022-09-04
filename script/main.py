import datetime
import re
from zhconv import convert
import pymysql

tags = '''
		<h4>主題</h4>
		<div class="row genre-box">
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/62">折磨</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5g">嘔吐</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/59">觸手</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/57">蠻橫嬌羞</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/52">處男</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4y">正太控</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4r">出軌</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4e">瘙癢</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4d">運動</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4a">女同接吻</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/49">性感的</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/44">美容院</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/41">處女</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/40">爛醉如泥的</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3x">殘忍畫面</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3w">妄想</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3v">惡作劇</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3t">學校作品</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3r">粗暴</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3g">通姦</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3e">姐妹</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3d">雙性人</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3c">跳舞</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3b">性奴</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/37">倒追</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/35">性騷擾</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2y">其他</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2x">戀腿癖</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2v">偷窥</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2t">花癡</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2r">男同性恋</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2e">情侶</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2d">戀乳癖</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/20">亂倫</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1y">其他戀物癖</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1u">偶像藝人</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1i">野外・露出</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1e">獵豔</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1d">女同性戀</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/11">企畫</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6h">10枚組</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/61">科幻</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6i">女優ベスト・総集編</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6j">温泉</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6k">M男</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6l">原作コラボ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6n">16時間以上作品</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6o">デカチン・巨根</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6p">ファン感謝・訪問</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6q">動画</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6r">巨尻</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6s">ハーレム</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6t">日焼け</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6u">早漏</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6v">キス・接吻</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6w">汗だく</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/77">スマホ専用縦動画</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7d">Vシネマ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7c">Don Cipote's choice</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7f">アニメ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7g">アクション</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7h">イメージビデオ（男性）</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7i">孕ませ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7j">ボーイズラブ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7t">ビッチ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7u">特典あり（AVベースボール）</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7v">コミック雑誌</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7w">時間停止</a>
			</div>
			<h4>角色</h4>
		<div class="row genre-box">
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5w">黑幫成員</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5k">童年朋友</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5i">公主</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5f">亞洲女演員</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/58">伴侶</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4s">講師</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4l">婆婆</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4h">格鬥家</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3o">女檢察官</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/39">明星臉</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/38">女主人、女老板</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/34">模特兒</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/32">秘書</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/30">美少女</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2z">新娘、年輕妻子</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2w">姐姐</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2q">格鬥家</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2o">車掌小姐</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2f">寡婦</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2b">千金小姐</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2a">白人</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/29">已婚婦女</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/27">女醫生</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/26">各種職業</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/23">妓女</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/21">賽車女郎</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1x">女大學生</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1v">展場女孩</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1n">女教師</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1k">母親</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1c">家教</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/17">护士</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/10">蕩婦</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/z">黑人演員</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/p">女生</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/c">女主播</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/b">高中女生</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7">服務生</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5r">魔法少女</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/65">學生（其他）</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/64">動畫人物</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6c">遊戲的真人版</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6f">超級女英雄</a>
			</div>
			<h4>服裝</h4>
		<div class="row genre-box">
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5h">女戰士</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5c">及膝襪</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5b">娃娃</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/56">女忍者</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/55">女裝人妖</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/50">內衣</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4z">猥褻穿著</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4p">兔女郎</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4n">貓耳女</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4k">女祭司</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4i">泡泡襪</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3i">制服</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3a">緊身衣</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2s">裸體圍裙</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2m">迷你裙警察</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2l">空中小姐</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/28">連褲襪</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1w">身體意識</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/18">OL</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/12">和服・喪服</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/y">體育服</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/v">內衣</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/m">水手服</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/l">學校泳裝</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/k">旗袍</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/j">女傭</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/i">迷你裙</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/a">校服</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/9">泳裝</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/8">眼鏡</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1">角色扮演</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6b">哥德蘿莉</a>
			</div>
			<h4>體型</h4>
		<div class="row genre-box">
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5d">超乳</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4x">肌肉</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3n">乳房</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3k">嬌小的</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2k">屁股</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2i">高</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2g">變性者</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/22">無毛</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1t">胖女人</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1f">苗條</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/15">孕婦</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/13">成熟的女人</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/w">蘿莉塔</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/t">貧乳・微乳</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e">巨乳</a>
			</div>
			<h4>行為</h4>
		<div class="row genre-box">
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4w">顏面騎乘</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4t">食糞</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4j">足交</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/47">母乳</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/46">手指插入</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/45">按摩</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/42">女上位</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3q">舔陰</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3l">拳交</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3f">深喉</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2h">69</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/24">淫語</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1z">潮吹</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1s">乳交</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1r">排便</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1p">飲尿</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1o">口交</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1j">濫交</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/19">放尿</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/x">打手槍</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/u">吞精</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/r">肛交</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/n">顏射</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h">自慰</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5">顏射</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4">中出</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6m">肛内中出</a>
			</div>
			<h4>玩法</h4>
		<div class="row genre-box">
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/51">立即口交</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4f">女優按摩棒</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4c">子宮頸</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4b">催眠</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3z">乳液</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3y">羞恥</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3s">凌辱</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3p">拘束</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3m">輪姦</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3h">插入異物</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/36">鴨嘴</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2u">灌腸</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/25">監禁</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1q">紧缚</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1m">強姦</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1l">藥物</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1h">汽車性愛</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1b">SM</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1a">糞便</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/14">玩具</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/q">跳蛋</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/d">緊縛</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6">按摩棒</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3">多P</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5m">性愛</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5y">假陽具</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/63">逆強姦</a>
			</div>
			<h4>類別</h4>
		<div class="row genre-box">
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/60">合作作品</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5z">恐怖</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5t">給女性觀眾</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5s">教學</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5q">DMM專屬</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5o">R-15</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5n">R-18</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5e">3D</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/5a">特效</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/54">故事集</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/53">限時降價</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4v">複刻版</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4u">戲劇</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4q">戀愛</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4o">高畫質</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4m">主觀視角</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/48">介紹影片</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/43">4小時以上作品</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3u">薄馬賽克</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/3j">經典</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/33">首次亮相</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/31">數位馬賽克</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2p">投稿</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2n">纪录片</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2c">國外進口</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/1g">第一人稱攝影</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/16">業餘</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/s">局部特寫</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/o">獨立製作</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/g">DMM獨家</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f">單體作品</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2">合集</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hd">高清</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/sub">字幕</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/2j">天堂TV</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/4g">DVD多士爐</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/66">AV OPEN 2014 スーパーヘビー</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/69">AV OPEN 2014 ヘビー級</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/6a">AV OPEN 2014 ミドル級</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/70">AV OPEN 2015 マニア/フェチ部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/71">AV OPEN 2015 熟女部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/72">AV OPEN 2015 企画部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/73">AV OPEN 2015 乙女部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/74">AV OPEN 2015 素人部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/75">AV OPEN 2015 SM/ハード部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/76">AV OPEN 2015 女優部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7k">AVOPEN2016人妻・熟女部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7l">AVOPEN2016企画部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7m">AVOPEN2016ハード部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7n">AVOPEN2016マニア・フェチ部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7o">AVOPEN2016乙女部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7p">AVOPEN2016女優部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7q">AVOPEN2016ドラマ・ドキュメンタリー部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7r">AVOPEN2016素人部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7s">AVOPEN2016バラエティ部門</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7x">VR専用</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7y">堵嘴·喜劇</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/7z">幻想</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/80">性別轉型·女性化</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/81">為智能手機推薦垂直視頻</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/82">設置項目</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/83">迷你係列</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/84">體驗懺悔</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/85">黑暗系統</a>
			</div>
			<h4>其他</h4>
		<div class="row genre-box">
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/dx">オナサポ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/dy">アスリート</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/dz">覆面・マスク</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e0">ハイクオリティVR</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e1">ヘルス・ソープ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e2">ホテル</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e3">アクメ・オーガズム</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e4">花嫁</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e5">デート</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e6">軟体</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e7">娘・養女</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e8">スパンキング</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/e9">スワッピング・夫婦交換</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ea">部下・同僚</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/eb">旅行</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ec">胸チラ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ed">バック</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ee">エロス</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ef">男の潮吹き</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/eg">女上司</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/eh">セクシー</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ei">受付嬢</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ej">ノーブラ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ek">白目・失神</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/el">M女</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/em">女王様</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/en">ノーパン</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/eo">セレブ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ep">病院・クリニック</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/eq">面接</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/er">お風呂</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/es">叔母さん</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/et">罵倒</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/eu">お爺ちゃん</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ev">逆レイプ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ew">ディルド</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ex">ヨガ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ey">飲み会・合コン</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ez">部活・マネージャー</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f0">お婆ちゃん</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f1">ビジネススーツ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f2">チアガール</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f3">ママ友</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f4">エマニエル</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f5">妄想族</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f6">蝋燭</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f7">鼻フック</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f8">放置</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/f9">サンプル動画</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fa">サイコ・スリラー</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fb">ラブコメ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fc">オタク</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fd">4K</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fe">福袋</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ff">玩具責め</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fj">女優</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fl">お掃除フェラ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fn">筆おろし</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fq">美脚</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ft">美尻</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fu">フェチ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fw">羞恥プレイ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fx">逆ナンパ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/fy">着衣</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/g0">誘惑</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/g1">チラリズム</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/g2">写真集</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/g3">書籍版</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/g4">SM拘束</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/g6">ドキュメント</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/g9">グッズ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ga">Tシャツ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gb">失禁</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gc">オナホール</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gd">カップホール</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ge">非貫通</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gf">ローション付き</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gh">配信専用</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gi">官能小説</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gj">CD</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gk">生挿入</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gl">風俗</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gm">外国人</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gn">淫語責め</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/go">アナルファック</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gp">脚コキ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gq">オイルプレイ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gr">キャバ嬢</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gs">調教</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gt">ローション・オイル</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gu">媚薬</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gv">顔面騎乗位</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gw">微乳</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gx">デカチン</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/gz">野外</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h0">露出</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h1">レイプ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h2">ベスト、総集編</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h3">マジックミラー号</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h4">看護師</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h5">アイドル</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h6">妹</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h7">スクール水着</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h8">デリヘル</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/h9">凌辱</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ha">童貞モノ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hb">ブルマ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hc">ホテヘル</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hd">縛り</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/he">ロリータ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hf">ミニスカート</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hg">尻</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hh">奴隷</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hi">芸能人</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hj">下着</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hk">母親</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hl">湯</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hm">教師</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hn">ストッキング</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ho">電車、バス</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hp">健康診断</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hq">野球拳</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hr">浴衣</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hs">爆乳</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/ht">ソープ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hu">スワッピング</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hv">メガネ</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hw">素股</a>
	            
			<a class="col-lg-2 col-md-2 col-sm-3 col-xs-6 text-center" href="https://www.javbus.com/genre/hx">ドリンク</a>
			</div>
</div>
'''

insert = '''
INSERT INTO tag (NAME,GROUP_NAME,SOURCE, CREATED_TIME, UPDATED_TIME) VALUES (%s,%s,%s,%s,%s);
'''
jav = '001'


# <h4>主題</h4>
# INSERT INTO TAG (NAME,BIG_NAME) VALUES ()
def insert_tag(db_cursor):
    tag_re = re.compile(r'>.*</a>')
    theme_re = re.compile(r'>.*</h4>')
    lines = tags.split('\n')
    group_name = ''
    name = ''
    for line in lines:
        theme_list = theme_re.findall(line)
        for theme in theme_list:
            # group_name = convert(theme[1: -5], 'zh-cn')
            group_name = theme[1: -5]
        tag_list = tag_re.findall(line)
        for tag in tag_list:
            print(tag)
            name = tag[1: -4]
            print('group name:', group_name)
            print('tag name:', name)
            db_cursor.execute(insert, (name, group_name, jav, datetime.datetime.now(), datetime.datetime.now()))


if __name__ == "__main__":
    try:

        connect = pymysql.connect(host='localhost', port=3306, user='root',
                                  passwd='''12345678''', db='partridge')

        cursor = connect.cursor()
        print('------------------ succeed to connect database ------------------')
        insert_tag(cursor)
        connect.commit()

        cursor.close()
        connect.close()

    except Exception as e:
        print('------------------ fail to connect database ------------------', e)
