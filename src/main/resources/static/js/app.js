// 毕业记录管理系统前端JavaScript代码

// API基础URL
const API_BASE = '/api';

// 全局变量
let currentTab = 'students';

// 页面初始化
document.addEventListener('DOMContentLoaded', function() {
    // 初始化标签页
    showTab('students');

    // 加载初始数据
    loadStudents();
    loadCourses();

    // 绑定回车键事件
    bindEnterKeyEvents();
});

// 标签页切换
function showTab(tabName) {
    // 隐藏所有标签页内容
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => tab.classList.remove('active'));

    // 移除所有标签按钮的激活状态
    const buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(button => button.classList.remove('active'));

    // 显示选中的标签页
    document.getElementById(tabName + '-tab').classList.add('active');
    event.target.classList.add('active');

    currentTab = tabName;

    // 根据标签页加载相应数据
    switch(tabName) {
        case 'students':
            loadStudents();
            break;
        case 'courses':
            loadCourses();
            break;
        case 'statistics':
            loadStatistics();
            break;
    }
}

// 绑定回车键事件
function bindEnterKeyEvents() {
    // 新建学生输入框
    document.getElementById('newStudentId').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            createStudent();
        }
    });

    // 成绩管理输入框
    document.getElementById('gradeStudentId').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            document.getElementById('gradeValue').focus();
        }
    });

    document.getElementById('gradeValue').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            addGrade();
        }
    });
}

// API调用辅助函数
async function apiCall(endpoint, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const finalOptions = { ...defaultOptions, ...options };

    try {
        const response = await fetch(API_BASE + endpoint, finalOptions);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('API call failed:', error);
        throw error;
    }
}

// 消息显示函数
function showMessage(message, type = 'info') {
    const messageArea = document.getElementById('messageArea');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;

    messageArea.appendChild(messageDiv);
    messageArea.style.display = 'block';

    // 3秒后自动隐藏
    setTimeout(() => {
        messageDiv.remove();
        if (messageArea.children.length === 0) {
            messageArea.style.display = 'none';
        }
    }, 3000);
}

// 学生管理功能
async function createStudent() {
    const studentId = document.getElementById('newStudentId').value.trim();

    if (!studentId) {
        showMessage('请输入学生ID', 'error');
        return;
    }

    try {
        const student = await apiCall('/students', {
            method: 'POST',
            body: JSON.stringify({ studentId })
        });

        showMessage(`学生 ${student.studentId} 创建成功`, 'success');
        document.getElementById('newStudentId').value = '';
        loadStudents();
    } catch (error) {
        showMessage('创建学生失败: ' + error.message, 'error');
    }
}

async function loadStudents() {
    const container = document.getElementById('studentsList');
    container.innerHTML = '<p class="loading">正在加载学生列表...</p>';

    try {
        const students = await apiCall('/students');

        if (students.length === 0) {
            container.innerHTML = '<p class="loading">暂无学生数据</p>';
            return;
        }

        container.innerHTML = '';
        students.forEach((student, index) => {
            const studentCard = createStudentCard(student, index + 1);
            container.appendChild(studentCard);
        });
    } catch (error) {
        container.innerHTML = '<p class="loading">加载失败: ' + error.message + '</p>';
        showMessage('加载学生列表失败', 'error');
    }
}

function createStudentCard(student, rank) {
    const card = document.createElement('div');
    card.className = 'student-card';

    const averageGrade = student.averageGrade ? student.averageGrade.toFixed(2) : '0.00';
    const gradeCount = student.grades ? student.grades.length : 0;

    card.innerHTML = `
        <div class="student-header">
            <div>
                <span class="student-id">#${rank} ${student.studentId}</span>
            </div>
            <div class="average-grade">
                平均分: ${averageGrade}
            </div>
        </div>
        <div class="student-details">
            成绩数量: ${gradeCount}
        </div>
        <div class="student-actions">
            <button class="btn btn-info btn-small" onclick="viewGrades('${student.studentId}')">
                查看成绩
            </button>
            <button class="btn btn-danger btn-small" onclick="deleteStudent('${student.studentId}')">
                删除
            </button>
        </div>
    `;

    return card;
}

async function addGrade() {
    const studentId = document.getElementById('gradeStudentId').value.trim();
    const gradeValue = parseFloat(document.getElementById('gradeValue').value);

    if (!studentId) {
        showMessage('请输入学生ID', 'error');
        return;
    }

    if (isNaN(gradeValue) || gradeValue < 0 || gradeValue > 100) {
        showMessage('请输入有效的成绩 (0-100)', 'error');
        return;
    }

    try {
        const student = await apiCall(`/students/${studentId}/grades`, {
            method: 'POST',
            body: JSON.stringify({ gradeValue })
        });

        showMessage(`成绩 ${gradeValue} 已添加到学生 ${studentId}`, 'success');
        document.getElementById('gradeStudentId').value = '';
        document.getElementById('gradeValue').value = '';
        loadStudents();
    } catch (error) {
        showMessage('添加成绩失败: ' + error.message, 'error');
    }
}

async function viewGrades(studentId) {
    if (!studentId) {
        showMessage('请选择一个学生', 'error');
        return;
    }

    try {
        const response = await apiCall(`/students/${studentId}/grades`);
        const grades = response.grades;
        const statistics = response.statistics;

        showGradesModal(studentId, grades, statistics);
    } catch (error) {
        showMessage('查看成绩失败: ' + error.message, 'error');
    }
}

function showGradesModal(studentId, grades, statistics) {
    const modal = document.getElementById('gradesModal');
    const title = document.getElementById('gradesModalTitle');
    const content = document.getElementById('gradesModalContent');

    title.textContent = `学生 ${studentId} 的成绩详情`;

    let html = `
        <div style="margin-bottom: 20px;">
            <h3>统计信息</h3>
            <p><strong>成绩数量:</strong> ${statistics.gradeCount}</p>
            <p><strong>平均分:</strong> ${statistics.calculatedAverage ? statistics.calculatedAverage.toFixed(2) : '0.00'}</p>
            <p><strong>最高分:</strong> ${statistics.highestGrade}</p>
            <p><strong>最低分:</strong> ${statistics.lowestGrade}</p>
        </div>
        <div>
            <h3>详细成绩</h3>
    `;

    if (grades.length === 0) {
        html += '<p>暂无成绩记录</p>';
    } else {
        html += '<div style="display: flex; flex-wrap: wrap; gap: 10px;">';
        grades.forEach((grade, index) => {
            html += `<span style="background: #e2e8f0; padding: 5px 10px; border-radius: 15px; font-size: 14px;">
                        ${index + 1}. ${grade.gradeValue}
                     </span>`;
        });
        html += '</div>';
    }

    html += '</div>';
    content.innerHTML = html;
    modal.style.display = 'flex';
}

function closeModal() {
    document.getElementById('gradesModal').style.display = 'none';
}

async function deleteStudent(studentId) {
    if (!confirm(`确定要删除学生 ${studentId} 吗？此操作不可撤销。`)) {
        return;
    }

    try {
        const response = await fetch(API_BASE + `/students/${studentId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showMessage(`学生 ${studentId} 已删除`, 'success');
            loadStudents();
        } else {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
    } catch (error) {
        showMessage('删除学生失败: ' + error.message, 'error');
    }
}

// 课程管理功能
async function loadCourses() {
    try {
        const courses = await apiCall('/courses');
        const select = document.getElementById('courseSelect');

        // 清空现有选项（保留默认选项）
        select.innerHTML = '<option value="">选择课程</option>';

        courses.forEach(course => {
            const option = document.createElement('option');
            option.value = course.course.courseId;
            option.textContent = `${course.course.courseId} (${course.course.academicYear}) - ${course.studentCount} 名学生`;
            select.appendChild(option);
        });
    } catch (error) {
        showMessage('加载课程列表失败', 'error');
    }
}

async function loadCourseStudents() {
    const courseId = document.getElementById('courseSelect').value;
    const container = document.getElementById('courseStudentsList');

    if (!courseId) {
        container.innerHTML = '<p class="loading">请选择课程查看学生列表</p>';
        return;
    }

    container.innerHTML = '<p class="loading">正在加载课程学生列表...</p>';

    try {
        const students = await apiCall(`/courses/${courseId}/students`);

        if (students.length === 0) {
            container.innerHTML = '<p class="loading">该课程暂无学生</p>';
            return;
        }

        container.innerHTML = '';
        students.forEach((student, index) => {
            const studentCard = createCourseStudentCard(student, index + 1, courseId);
            container.appendChild(studentCard);
        });
    } catch (error) {
        container.innerHTML = '<p class="loading">加载失败: ' + error.message + '</p>';
        showMessage('加载课程学生列表失败', 'error');
    }
}

function createCourseStudentCard(student, rank, courseId) {
    const card = document.createElement('div');
    card.className = 'student-card';

    const averageGrade = student.averageGrade ? student.averageGrade.toFixed(2) : '0.00';
    const gradeCount = student.grades ? student.grades.length : 0;

    card.innerHTML = `
        <div class="student-header">
            <div>
                <span class="student-id">#${rank} ${student.studentId}</span>
            </div>
            <div class="average-grade">
                平均分: ${averageGrade}
            </div>
        </div>
        <div class="student-details">
            成绩数量: ${gradeCount}
        </div>
        <div class="student-actions">
            <button class="btn btn-danger btn-small" onclick="removeStudentFromCourse('${courseId}', '${student.studentId}')">
                从课程移除
            </button>
        </div>
    `;

    return card;
}

async function addStudentToCourse() {
    const courseId = document.getElementById('courseSelect').value;
    const studentId = document.getElementById('courseStudentId').value.trim();

    if (!courseId) {
        showMessage('请选择课程', 'error');
        return;
    }

    if (!studentId) {
        showMessage('请输入学生ID', 'error');
        return;
    }

    try {
        await apiCall(`/courses/${courseId}/students/${studentId}`, {
            method: 'POST'
        });

        showMessage(`学生 ${studentId} 已添加到课程 ${courseId}`, 'success');
        document.getElementById('courseStudentId').value = '';
        loadCourseStudents();
        loadCourses(); // 刷新课程统计
    } catch (error) {
        showMessage('添加学生到课程失败: ' + error.message, 'error');
    }
}

async function removeStudentFromCourse(courseId, studentId) {
    try {
        await apiCall(`/courses/${courseId}/students/${studentId}`, {
            method: 'DELETE'
        });

        showMessage(`学生 ${studentId} 已从课程 ${courseId} 移除`, 'success');
        loadCourseStudents();
        loadCourses(); // 刷新课程统计
    } catch (error) {
        showMessage('从课程移除学生失败: ' + error.message, 'error');
    }
}

async function updateStudentGrades() {
    const courseId = document.getElementById('updateCourseId').value.trim();
    const studentId = document.getElementById('updateStudentId').value.trim();
    const gradesText = document.getElementById('updateGrades').value.trim();

    if (!courseId || !studentId || !gradesText) {
        showMessage('请填写所有字段', 'error');
        return;
    }

    // 解析成绩字符串 (用逗号分隔)
    const grades = gradesText.split(',').map(g => {
        const grade = parseFloat(g.trim());
        if (isNaN(grade) || grade < 0 || grade > 100) {
            throw new Error('无效的成绩值: ' + g.trim());
        }
        return grade;
    });

    try {
        await apiCall(`/courses/${courseId}/students/${studentId}/grades`, {
            method: 'PUT',
            body: JSON.stringify(grades)
        });

        showMessage(`学生 ${studentId} 的成绩已更新`, 'success');
        document.getElementById('updateCourseId').value = '';
        document.getElementById('updateStudentId').value = '';
        document.getElementById('updateGrades').value = '';
        loadCourseStudents();
        loadStudents();
    } catch (error) {
        showMessage('更新成绩失败: ' + error.message, 'error');
    }
}

// 统计信息功能
async function loadStatistics() {
    const container = document.getElementById('statisticsContainer');
    container.innerHTML = '<p class="loading">正在加载统计信息...</p>';

    try {
        // 获取所有课程统计
        const courses = await apiCall('/courses');

        // 计算总体统计
        let totalStudents = 0;
        let totalCourses = courses.length;

        courses.forEach(course => {
            totalStudents += course.studentCount;
        });

        // 获取健康检查信息
        const health = await apiCall('/health');

        container.innerHTML = `
            <div class="stat-card">
                <div class="stat-value">${totalCourses}</div>
                <div class="stat-label">总课程数</div>
            </div>
            <div class="stat-card">
                <div class="stat-value">${totalStudents}</div>
                <div class="stat-label">总学生数</div>
            </div>
            <div class="stat-card">
                <div class="stat-value">${health.version}</div>
                <div class="stat-label">系统版本</div>
            </div>
            <div class="stat-card">
                <div class="stat-value">${health.status}</div>
                <div class="stat-label">系统状态</div>
            </div>
        `;

        // 显示详细的课程统计
        if (courses.length > 0) {
            let detailsHtml = '<div style="margin-top: 30px;"><h3>课程详情</h3>';
            courses.forEach(course => {
                detailsHtml += `
                    <div style="background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 8px;">
                        <strong>${course.course.courseId}</strong> (${course.course.academicYear})
                        - 学生数: ${course.studentCount}
                    </div>
                `;
            });
            detailsHtml += '</div>';
            container.innerHTML += detailsHtml;
        }

    } catch (error) {
        container.innerHTML = '<p class="loading">加载统计信息失败: ' + error.message + '</p>';
        showMessage('加载统计信息失败', 'error');
    }
}

// 点击模态框外部关闭
window.onclick = function(event) {
    const modal = document.getElementById('gradesModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};
