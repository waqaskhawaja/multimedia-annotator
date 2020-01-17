import { NgModule } from '@angular/core';

import { MaSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [MaSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [MaSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class MaSharedCommonModule {}
