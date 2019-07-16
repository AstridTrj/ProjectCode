import sys
import dategame
from PyQt5.QtWidgets import (QWidget, QCalendarWidget, QLabel, QApplication)
from PyQt5.QtCore import QDate
from PyQt5.QtGui import QFont, QPalette, QColor


class Calendar(QWidget):
    # 继承QWindget类
    def __init__(self):
        super().__init__()

        self.initUI()

    def initUI(self):
        # 定义一个日历
        cal = QCalendarWidget(self)
        # 设置初始显示日期
        cal.setCurrentPage(1900, 1)
        # 设置网格显示
        cal.setGridVisible(True)
        cal.move(20, 50)
        # 将日历的点击信号转化为QDate格式，传入操作函数
        cal.clicked[QDate].connect(self.showDate)

        # 定义字体类型，大小，颜色
        self.font = QFont('Microsoft YaHei')
        self.font.setPointSize(12)
        self.colr = QPalette()
        self.colr.setColor(QPalette.WindowText, QColor(255,0,0))

        # 字体标签
        self.title = QLabel(self)
        self.title.setText('选择1900到2001的日期')
        self.title.setFont(self.font)
        self.title.move(65, 20)

        self.lbl = QLabel(self)
        self.lbl.setText('     亚当先行\n是否可赢得比赛')
        self.lbl.setFont(self.font)
        self.lbl.move(310, 80)

        # 结果显示
        self.result = QLabel(self)
        self.result.setFont(self.font)
        self.result.setPalette(self.colr)
        self.result.move(345, 150)

        self.setGeometry(300, 300, 450, 280)
        self.setWindowTitle('Calendar')
        self.show()

    def showDate(self, date):
        # 得到年月日
        year = int(date.toString().split(' ')[3])
        month = int(date.toString().split(' ')[1][:-1])
        day = int(date.toString().split(' ')[2])

        # 调用函数计算结果
        result = dategame.main(year, month, day)
        # 在面板显示结果
        self.result.setText(result)
        self.result.adjustSize()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = Calendar()
    sys.exit(app.exec_())