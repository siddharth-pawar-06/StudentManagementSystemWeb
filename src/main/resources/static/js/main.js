// Student Management System - Frontend Interactivity

document.addEventListener('DOMContentLoaded', function () {
    // 1. Auto-calculate and display grade dynamically as user types marks in Add/Edit forms
    const marksInput = document.getElementById('marks');
    const gradePreview = document.getElementById('gradePreview');

    function calculateGrade(marks) {
        if (isNaN(marks) || marks < 0 || marks > 100) return '--';
        if (marks >= 90) return 'A+';
        if (marks >= 80) return 'A';
        if (marks >= 70) return 'B';
        if (marks >= 60) return 'C';
        if (marks >= 40) return 'D';
        return 'F';
    }

    function updateGradePreview() {
        if (!marksInput || !gradePreview) return;
        const val = parseFloat(marksInput.value);
        const grade = calculateGrade(val);
        gradePreview.textContent = grade;

        // Reset badge classes
        gradePreview.className = 'badge';
        if (grade === 'A+' || grade === 'A') {
            gradePreview.classList.add('bg-success');
        } else if (grade === 'B') {
            gradePreview.classList.add('bg-info', 'text-dark');
        } else if (grade === 'C') {
            gradePreview.classList.add('bg-warning', 'text-dark');
        } else if (grade === 'D') {
            gradePreview.classList.add('bg-secondary');
        } else if (grade === 'F') {
            gradePreview.classList.add('bg-danger');
        } else {
            gradePreview.classList.add('bg-light', 'text-muted');
        }
    }

    if (marksInput) {
        marksInput.addEventListener('input', updateGradePreview);
        updateGradePreview();
    }

    // 2. Delete confirmation modal / prompt
    const deleteButtons = document.querySelectorAll('.btn-delete-confirm');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function (e) {
            const studentId = this.getAttribute('data-id');
            const studentName = this.getAttribute('data-name');
            const confirmed = confirm(`Are you sure you want to delete student: "${studentName}" (ID: ${studentId})?\nThis action cannot be undone.`);
            if (!confirmed) {
                e.preventDefault();
            }
        });
    });

    // 3. Auto dismiss alert messages after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            try {
                const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
                bsAlert.close();
            } catch (e) {
                // Ignore if bootstrap JS not loaded
            }
        }, 5000);
    });
});
