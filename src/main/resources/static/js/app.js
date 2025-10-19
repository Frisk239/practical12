// Graduation Records Management System Frontend JavaScript Code

// API Base URL
const API_BASE = '/api';

// Global variables
let currentTab = 'students';

// Page initialization
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tabs
    showTab('students');

    // Load initial data
    loadStudents();
    loadCourses();

    // Bind enter key events
    bindEnterKeyEvents();
});

// Tab switching
function showTab(tabName) {
    // Hide all tab content
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => tab.classList.remove('active'));

    // Remove active state from all tab buttons
    const buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(button => button.classList.remove('active'));

    // Show selected tab
    document.getElementById(tabName + '-tab').classList.add('active');
    event.target.classList.add('active');

    currentTab = tabName;

    // Load corresponding data based on tab
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

// Bind enter key events
function bindEnterKeyEvents() {
    // New student input field
    document.getElementById('newStudentId').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            createStudent();
        }
    });
}

// API call helper function
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

// Message display function
function showMessage(message, type = 'info') {
    const messageArea = document.getElementById('messageArea');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;

    messageArea.appendChild(messageDiv);
    messageArea.style.display = 'block';

    // Hide after 3 seconds
    setTimeout(() => {
        messageDiv.remove();
        if (messageArea.children.length === 0) {
            messageArea.style.display = 'none';
        }
    }, 3000);
}

// Student Management Functions
async function createStudent() {
    const studentId = document.getElementById('newStudentId').value.trim();

    if (!studentId) {
        showMessage('Please enter Student ID (请输入学生ID)', 'error');
        return;
    }

    try {
        const student = await apiCall('/students', {
            method: 'POST',
            body: JSON.stringify({ studentId })
        });

        showMessage(`Student ${student.studentId} created successfully (学生 ${student.studentId} 创建成功)`, 'success');
        document.getElementById('newStudentId').value = '';
        loadStudents();
    } catch (error) {
        showMessage('Failed to create student (创建学生失败): ' + error.message, 'error');
    }
}

async function loadStudents() {
    const container = document.getElementById('studentsList');
    container.innerHTML = '<p class="loading">Loading student list... (正在加载学生列表)</p>';

    try {
        const students = await apiCall('/students');

        if (students.length === 0) {
        container.innerHTML = '<p class="loading">No student data (暂无学生数据)</p>';
            return;
        }

        container.innerHTML = '';
        students.forEach((student, index) => {
            const studentCard = createStudentCard(student, index + 1);
            container.appendChild(studentCard);
        });
    } catch (error) {
        container.innerHTML = '<p class="loading">Loading failed (加载失败): ' + error.message + '</p>';
        showMessage('Failed to load student list (加载学生列表失败)', 'error');
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
                Average Grade (平均分): ${averageGrade}
            </div>
        </div>
        <div class="student-details">
            Grade Count (成绩数量): ${gradeCount}
        </div>
        <div class="student-actions">
            <button class="btn btn-info btn-small" onclick="viewGrades('${student.studentId}')">
                View Grades (查看成绩)
            </button>
            <button class="btn btn-danger btn-small" onclick="deleteStudent('${student.studentId}')">
                Delete (删除)
            </button>
        </div>
    `;

    return card;
}



async function viewGrades(studentId) {
    if (!studentId) {
        showMessage('Please select a student (请选择一个学生)', 'error');
        return;
    }

    try {
        console.log('Fetching grades for student:', studentId);
        const response = await apiCall(`/students/${studentId}/grades`);
        console.log('API Response:', response);

        const gradesByCourse = response.gradesByCourse;
        const statistics = response.statistics;

        console.log('gradesByCourse:', gradesByCourse);
        console.log('statistics:', statistics);

        showGradesModal(studentId, gradesByCourse, statistics);
    } catch (error) {
        console.error('Error in viewGrades:', error);
        showMessage('Failed to view grades (查看成绩失败): ' + error.message, 'error');
    }
}

function showGradesModal(studentId, gradesByCourse, statistics) {
    const modal = document.getElementById('gradesModal');
    const title = document.getElementById('gradesModalTitle');
    const content = document.getElementById('gradesModalContent');

    title.textContent = `Student ${studentId} Grade Details (学生 ${studentId} 的成绩详情)`;

    let html = `
        <div style="margin-bottom: 20px;">
            <h3>Statistics (统计信息)</h3>
            <p><strong>Grade Count (成绩数量):</strong> ${statistics.gradeCount}</p>
            <p><strong>Average Grade (平均分):</strong> ${statistics.calculatedAverage ? statistics.calculatedAverage.toFixed(2) : '0.00'}</p>
            <p><strong>Highest Grade (最高分):</strong> ${statistics.highestGrade}</p>
            <p><strong>Lowest Grade (最低分):</strong> ${statistics.lowestGrade}</p>
        </div>
        <div>
            <h3>Grades by Course (各科成绩)</h3>
    `;

    if (Object.keys(gradesByCourse).length === 0) {
        html += '<p>No grade records (暂无成绩记录)</p>';
    } else {
        html += '<div style="display: flex; flex-direction: column; gap: 15px;">';
        Object.entries(gradesByCourse).forEach(([courseId, grades]) => {
            html += `<div style="border: 1px solid #ddd; padding: 10px; border-radius: 8px;">
                        <strong>${courseId}:</strong> `;
            if (grades.length === 0) {
                html += `<span style="color: #666; font-style: italic;">No grades (暂无成绩)</span>`;
            } else {
                grades.forEach((grade, index) => {
                    html += `<span style="background: #e2e8f0; padding: 3px 8px; border-radius: 12px; font-size: 14px; margin: 2px;">
                                ${grade.toFixed(1)}
                             </span>`;
                });
            }
            html += '</div>';
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
    if (!confirm(`Are you sure you want to delete student ${studentId}? This action cannot be undone. (确定要删除学生 ${studentId} 吗？此操作不可撤销。)`)) {
        return;
    }

    try {
        const response = await fetch(API_BASE + `/students/${studentId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showMessage(`Student ${studentId} has been deleted (学生 ${studentId} 已删除)`, 'success');
            loadStudents();
        } else {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
    } catch (error) {
        showMessage('Failed to delete student (删除学生失败): ' + error.message, 'error');
    }
}

// Course Management Functions
async function createCourse() {
    const courseId = document.getElementById('newCourseId').value.trim();
    const academicYear = document.getElementById('newAcademicYear').value.trim();

    if (!courseId || !academicYear) {
        showMessage('Please fill in Course ID and Academic Year (请填写课程ID和学年)', 'error');
        return;
    }

    try {
        const course = await apiCall('/courses', {
            method: 'POST',
            body: JSON.stringify({ courseId, academicYear })
        });

        showMessage(`Course ${course.courseId} created successfully (课程 ${course.courseId} 创建成功)`, 'success');
        document.getElementById('newCourseId').value = '';
        document.getElementById('newAcademicYear').value = '';
        loadCourses(); // Refresh course list
    } catch (error) {
        showMessage('Failed to create course (创建课程失败): ' + error.message, 'error');
    }
}

async function loadCourses() {
    try {
        const courses = await apiCall('/courses');
        const select = document.getElementById('courseSelect');
        const deleteSelect = document.getElementById('deleteCourseSelect');

        // Clear existing options (keep default option)
        select.innerHTML = '<option value="">Select Course (选择课程)</option>';
        deleteSelect.innerHTML = '<option value="">Select course to delete (选择要删除的课程)</option>';

        courses.forEach(course => {
            const option = document.createElement('option');
            option.value = course.course.courseId;
            option.textContent = `${course.course.courseId} (${course.course.academicYear}) - ${course.studentCount} students (名学生)`;
            select.appendChild(option);

            const deleteOption = document.createElement('option');
            deleteOption.value = course.course.courseId;
            deleteOption.textContent = `${course.course.courseId} (${course.course.academicYear})`;
            deleteSelect.appendChild(deleteOption);
        });
    } catch (error) {
        showMessage('Failed to load course list (加载课程列表失败)', 'error');
    }
}

async function deleteCourse() {
    const courseId = document.getElementById('deleteCourseSelect').value;

    if (!courseId) {
        showMessage('Please select a course to delete (请选择要删除的课程)', 'error');
        return;
    }

    if (!confirm(`Are you sure you want to delete course ${courseId}?\n\nThis will also delete:\n- All grade records for this course\n- All course enrollment records\n\nThis action cannot be undone! (确定要删除课程 ${courseId} 吗？\n\n这将同时删除：\n- 该课程的所有成绩记录\n- 该课程的所有选课记录\n\n此操作不可撤销！)`)) {
        return;
    }

    try {
        // DELETE request returns empty response body, use fetch directly instead of apiCall
        const response = await fetch(API_BASE + `/courses/${courseId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        showMessage(`Course ${courseId} has been deleted (课程 ${courseId} 已删除)`, 'success');
        document.getElementById('deleteCourseSelect').value = '';
        loadCourses(); // Refresh course list
        loadStatistics(); // Refresh statistics
    } catch (error) {
        showMessage('Failed to delete course (删除课程失败): ' + error.message, 'error');
    }
}

async function loadCourseStudents() {
    const courseId = document.getElementById('courseSelect').value;
    const container = document.getElementById('courseStudentsList');

    if (!courseId) {
        container.innerHTML = '<p class="loading">Please select a course to view student list (请选择课程查看学生列表)</p>';
        return;
    }

    container.innerHTML = '<p class="loading">Loading course student list... (正在加载课程学生列表)</p>';

    try {
        const students = await apiCall(`/courses/${courseId}/students`);

        if (students.length === 0) {
            container.innerHTML = '<p class="loading">No students in this course (该课程暂无学生)</p>';
            return;
        }

        container.innerHTML = '';
        students.forEach((student, index) => {
            const studentCard = createCourseStudentCard(student, index + 1, courseId);
            container.appendChild(studentCard);
        });
    } catch (error) {
        container.innerHTML = '<p class="loading">Loading failed (加载失败): ' + error.message + '</p>';
        showMessage('Failed to load course student list (加载课程学生列表失败)', 'error');
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
                Average Grade (平均分): ${averageGrade}
            </div>
        </div>
        <div class="student-details">
            Grade Count (成绩数量): ${gradeCount}
        </div>
        <div class="student-actions">
            <button class="btn btn-danger btn-small" onclick="removeStudentFromCourse('${courseId}', '${student.studentId}')">
                Remove from Course (从课程移除)
            </button>
        </div>
    `;

    return card;
}

async function addStudentToCourse() {
    const courseId = document.getElementById('courseSelect').value;
    const studentId = document.getElementById('courseStudentId').value.trim();

    if (!courseId) {
        showMessage('Please select a course (请选择课程)', 'error');
        return;
    }

    if (!studentId) {
        showMessage('Please enter Student ID (请输入学生ID)', 'error');
        return;
    }

    try {
        await apiCall(`/courses/${courseId}/students/${studentId}`, {
            method: 'POST'
        });

        showMessage(`Student ${studentId} has been added to course ${courseId} (学生 ${studentId} 已添加到课程 ${courseId})`, 'success');
        document.getElementById('courseStudentId').value = '';
        loadCourseStudents();
        loadCourses(); // Refresh course statistics
    } catch (error) {
        showMessage('Failed to add student to course (添加学生到课程失败): ' + error.message, 'error');
    }
}

async function removeStudentFromCourse(courseId, studentId) {
    try {
        await apiCall(`/courses/${courseId}/students/${studentId}`, {
            method: 'DELETE'
        });

        showMessage(`Student ${studentId} has been removed from course ${courseId} (学生 ${studentId} 已从课程 ${courseId} 移除)`, 'success');
        loadCourseStudents();
        loadCourses(); // Refresh course statistics
    } catch (error) {
        showMessage('Failed to remove student from course (从课程移除学生失败): ' + error.message, 'error');
    }
}

async function updateStudentGrades() {
    const courseId = document.getElementById('updateCourseId').value.trim();
    const studentId = document.getElementById('updateStudentId').value.trim();
    const gradesText = document.getElementById('updateGrades').value.trim();

    if (!courseId || !studentId || !gradesText) {
        showMessage('Please fill in all fields (请填写所有字段)', 'error');
        return;
    }

    // Parse grades string (comma separated)
    const grades = gradesText.split(',').map(g => {
        const grade = parseFloat(g.trim());
        if (isNaN(grade) || grade < 0 || grade > 100) {
            throw new Error('Invalid grade value (无效的成绩值): ' + g.trim());
        }
        return grade;
    });

    try {
        await apiCall(`/courses/${courseId}/students/${studentId}/grades`, {
            method: 'PUT',
            body: JSON.stringify(grades)
        });

        showMessage(`Student ${studentId}'s grades have been updated (学生 ${studentId} 的成绩已更新)`, 'success');
        document.getElementById('updateCourseId').value = '';
        document.getElementById('updateStudentId').value = '';
        document.getElementById('updateGrades').value = '';
        loadCourseStudents();
        loadStudents();
    } catch (error) {
        showMessage('Failed to update grades (更新成绩失败): ' + error.message, 'error');
    }
}

// Statistics Functions
async function loadStatistics() {
    const container = document.getElementById('statisticsContainer');
    container.innerHTML = '<p class="loading">Loading statistics... (正在加载统计信息)</p>';

    try {
        // Get all course statistics
        const courses = await apiCall('/courses');

        // Calculate overall statistics
        let totalStudents = 0;
        let totalCourses = courses.length;

        courses.forEach(course => {
            totalStudents += course.studentCount;
        });

        // Get health check information
        const health = await apiCall('/health');

        container.innerHTML = `
            <div class="stat-card">
                <div class="stat-value">${totalCourses}</div>
                <div class="stat-label">Total Courses (总课程数)</div>
            </div>
            <div class="stat-card">
                <div class="stat-value">${totalStudents}</div>
                <div class="stat-label">Total Course Enrollments (所有课程参加的学生总数)</div>
            </div>
        `;

        // Display detailed course statistics
        if (courses.length > 0) {
            let detailsHtml = '<div style="margin-top: 30px;"><h3>Course Details (课程详情)</h3>';
            courses.forEach(course => {
                detailsHtml += `
                    <div style="background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 8px;">
                        <strong>${course.course.courseId}</strong> (${course.course.academicYear})
                        - Student Count (学生数): ${course.studentCount}
                    </div>
                `;
            });
            detailsHtml += '</div>';
            container.innerHTML += detailsHtml;
        }

    } catch (error) {
        container.innerHTML = '<p class="loading">Failed to load statistics (加载统计信息失败): ' + error.message + '</p>';
        showMessage('Failed to load statistics (加载统计信息失败)', 'error');
    }
}

// Click outside modal to close
window.onclick = function(event) {
    const modal = document.getElementById('gradesModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};
