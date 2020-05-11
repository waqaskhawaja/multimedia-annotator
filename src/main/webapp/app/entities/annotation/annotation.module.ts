import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared/shared.module';
import { AnnotationComponent } from './annotation.component';
import { AnnotationDetailComponent } from './annotation-detail.component';
import { AnnotationUpdateComponent } from './annotation-update.component';
import { AnnotationDeleteDialogComponent } from './annotation-delete-dialog.component';
import { annotationRoute } from './annotation.route';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(annotationRoute), ReactiveFormsModule],
    declarations: [AnnotationComponent, AnnotationDetailComponent, AnnotationUpdateComponent, AnnotationDeleteDialogComponent],
    entryComponents: [AnnotationDeleteDialogComponent]
})
export class MaAnnotationModule {}
