import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MaSharedModule } from 'app/shared/shared.module';
import { AnnotationTypeComponent } from './annotation-type.component';
import { AnnotationTypeDetailComponent } from './annotation-type-detail.component';
import { AnnotationTypeUpdateComponent } from './annotation-type-update.component';
import { AnnotationTypeDeleteDialogComponent } from './annotation-type-delete-dialog.component';
import { annotationTypeRoute } from './annotation-type.route';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
    imports: [MaSharedModule, RouterModule.forChild(annotationTypeRoute), ReactiveFormsModule],
    declarations: [
        AnnotationTypeComponent,
        AnnotationTypeDetailComponent,
        AnnotationTypeUpdateComponent,
        AnnotationTypeDeleteDialogComponent
    ],
    entryComponents: [AnnotationTypeDeleteDialogComponent]
})
export class MaAnnotationTypeModule {}
